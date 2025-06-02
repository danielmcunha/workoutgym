package com.southapps.ui.screen.routine.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.common.useCase.FormResource
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.common.validation.form.toFormField
import com.southapps.domain.routine.entities.WorkoutRoutine
import com.southapps.domain.routine.entities.formattedScheduledTime
import com.southapps.domain.routine.useCases.RoutineCreateUseCase
import com.southapps.domain.routine.useCases.RoutineCreateInput
import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.workout.entities.WorkoutLocationType
import com.southapps.domain.workout.entities.WorkoutSummary
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private const val WORKOUT = "Treino"
private const val FREQUENCY_TYPE = "Frequência"
private const val DAYS_OF_WEEK = "Dias da semana"
private const val TIMES_PER_WEEK = "Vezes por semana"
private const val IS_PRESENTIAL = "Tipo de aula"
private const val SCHEDULED_TIME = "Horário"
private const val LOCATION_TYPE = "Tipo de local"
private const val LOCATION_DETAILS = "Detalhes do local"

data class RoutineCreateUiState(
    val isLoading: Boolean = false,
    val workouts: List<WorkoutSummary> = emptyList(),
    val selectedWorkout: FormField = FormField(WORKOUT, null),
    val frequencyType: FormField = FormField(FREQUENCY_TYPE, FrequencyType.TIMES_PER_WEEK),
    val daysOfWeek: FormField = FormField(DAYS_OF_WEEK, emptySet<DayOfWeek>()),
    val timesPerWeek: FormField = FormField(TIMES_PER_WEEK, 3),
    val isPresential: FormField = FormField(IS_PRESENTIAL, false),
    val scheduledTime: FormField = FormField(SCHEDULED_TIME, ""),
    val locationType: FormField = FormField(LOCATION_TYPE, null),
    val locationDetails: FormField = FormField(LOCATION_DETAILS, ""),
    val error: String? = null
) : UIState

class RoutineCreateViewModel(
    navigator: Navigator,
    private val routineSaveUseCase: RoutineCreateUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RoutineCreateUiState, UIAction>(navigator) {

    private val arguments = savedStateHandle.toRoute<WorkoutRoute.RoutineCreate>()

    private var routineId: String? = null

    private val _uiState = MutableStateFlow(RoutineCreateUiState())
    override val uiState = _uiState.onStart {
        val routine = arguments.routineJson?.run {
            Json.decodeFromString<WorkoutRoutine>(this)
        }

        routineId = routine?.uid

        val workouts = arguments.workoutsJson?.run {
            Json.decodeFromString(
                ListSerializer(WorkoutSummary.serializer()),
                this
            )
        } ?: emptyList()

        _uiState.update {
            it.copy(
                workouts = workouts,
                selectedWorkout = routine?.workoutSummary.toFormField(WORKOUT),
                frequencyType = (routine?.frequencyType
                    ?: FrequencyType.TIMES_PER_WEEK).toFormField(
                    TIMES_PER_WEEK
                ),
                daysOfWeek = (routine?.daysOfWeek ?: emptySet()).toFormField(DAYS_OF_WEEK),
                timesPerWeek = (routine?.timesPerWeek ?: 3).toFormField(TIMES_PER_WEEK),
                isPresential = (routine?.isPresential ?: false).toFormField(IS_PRESENTIAL),
                scheduledTime = routine?.formattedScheduledTime?.filterNot { char -> char == ':' }
                    .orEmpty().toFormField(SCHEDULED_TIME),
                locationType = routine?.locationType.toFormField(LOCATION_TYPE),
                locationDetails = routine?.locationDetails.orEmpty().toFormField(LOCATION_DETAILS)
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        RoutineCreateUiState()
    )

    fun updateFrequencyType(value: FrequencyType) {
        _uiState.update { it.copy(frequencyType = it.frequencyType.copy(value = value)) }
    }

    fun toggleDayOfWeek(day: DayOfWeek) {
        _uiState.update {
            val updated = (it.daysOfWeek.value as Set<DayOfWeek>).toMutableSet().apply {
                if (contains(day)) remove(day) else add(day)
            }
            it.copy(daysOfWeek = it.daysOfWeek.copy(value = updated))
        }
    }

    fun updateTimesPerWeek(value: Int) {
        _uiState.update { it.copy(timesPerWeek = it.timesPerWeek.copy(value = value)) }
    }

    fun updateIsPresential(value: Boolean) {
        _uiState.update { it.copy(isPresential = it.isPresential.copy(value = value)) }
    }

    fun updateScheduledTime(value: String) {
        _uiState.update {
            it.copy(
                scheduledTime = it.scheduledTime.copy(
                    value = value.filter { it.isDigit() }.take(4)
                )
            )
        }
    }

    fun updateLocationType(type: WorkoutLocationType) {
        _uiState.update { it.copy(locationType = it.locationType.copy(value = type)) }
    }

    fun updateLocationDetails(details: String) {
        _uiState.update { it.copy(locationDetails = it.locationDetails.copy(value = details)) }
    }

    fun updateSelectedWorkout(workout: WorkoutSummary) {
        _uiState.update { it.copy(selectedWorkout = it.selectedWorkout.copy(value = workout)) }
    }

    fun saveRoutine() {
        setLoading(true)

        viewModelScope.launch {
            routineSaveUseCase.execute(
                RoutineCreateInput(
                    chickenPath = arguments.chickenPath,
                    uid = routineId,
                    frequencyType = _uiState.value.frequencyType,
                    daysOfWeek = _uiState.value.daysOfWeek,
                    timesPerWeek = _uiState.value.timesPerWeek,
                    isPresential = _uiState.value.isPresential,
                    scheduledTime = _uiState.value.scheduledTime,
                    locationType = _uiState.value.locationType,
                    locationDetails = _uiState.value.locationDetails,
                    workoutSummary = _uiState.value.selectedWorkout
                )
            ).collect { result ->
                when (result) {
                    is FormResource.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception?.message ?: ""
                            )
                        }
                    }

                    is FormResource.FormError<*> -> {
                        var error = ""
                        result.errorFormFields.forEach { formField ->
                            error += formField.error + "\n"
                        }

                        _uiState.update {
                            it.copy(error = error)
                        }
                    }

                    is FormResource.Success<*> -> {
                        navigator.navigateBack(resultKey = "routine_updated", resultValue = true)
                    }
                }
            }
        }

        setLoading(false)
    }

    private fun setLoading(value: Boolean) {
        _uiState.update { it.copy(isLoading = value) }
    }
}
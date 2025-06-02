package com.southapps.ui.screen.workout.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.core.WorkoutCreateUiAction
import com.southapps.domain.common.useCase.FormResource
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.common.validation.form.toFormField
import com.southapps.domain.workout.entities.Workout
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.domain.workout.entities.WorkoutSaveInput
import com.southapps.domain.workout.useCases.WorkoutGetUseCase
import com.southapps.domain.workout.useCases.WorkoutSaveUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val WORKOUT_NAME = "Nome do treino"
private const val DESCRIPTION = "Descrição"
private const val WORKOUT_GOAL = "Objetivo do treino"
private const val ESTIMATED_DURATION = "Duração (min)"
private const val REQUIRED_EQUIPMENT = "Material necessário"
private const val MASTER_NOTES = "Notas"

/**
 * UiState for the Add/Edit screen
 */
data class WorkoutCreateUiState(
    val isLoading: Boolean = false,
    val workoutName: FormField = "".toFormField(WORKOUT_NAME),
    val description: FormField = "".toFormField(DESCRIPTION, optional = true),
    val workoutGoal: FormField = "".toFormField(WORKOUT_GOAL),
    val estimatedDuration: FormField = "".toFormField(ESTIMATED_DURATION, optional = true),
    val requiredEquipment: FormField = "".toFormField(REQUIRED_EQUIPMENT, optional = true),
    val masterNotes: FormField = "".toFormField(MASTER_NOTES, optional = true),
    val error: String? = null,
) : UIState

class WorkoutCreateViewModel(
    navigator: Navigator,
    private val workoutSaveUseCase: WorkoutSaveUseCase,
    private val workoutGetUseCase: WorkoutGetUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WorkoutCreateUiState, UIAction>(navigator) {

    private val arguments = savedStateHandle.toRoute<WorkoutRoute.WorkoutCreate>()

    private val _uiState = MutableStateFlow(WorkoutCreateUiState(isLoading = true))
    private var workoutId: String? = null
    override val uiState = _uiState.onStart {
        if (arguments.workoutPath != null) {
            fetchWorkout(arguments.workoutPath)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        WorkoutCreateUiState(isLoading = true)
    )

    private fun fetchWorkout(workoutReference: String) {
        viewModelScope.launch {
            workoutGetUseCase.execute(workoutReference).collect { result ->
                when (result) {
                    is Resource.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception?.message ?: ""
                            )
                        }
                    }

                    is Resource.Success<Workout> -> {
                        workoutId = result.data.uid
                        sendAction(WorkoutCreateUiAction.UpdateExercises(result.data.exercises))
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                workoutName = it.workoutName.copy(
                                    value = result.data.name
                                ),
                                description = it.description.copy(value = result.data.description.orEmpty()),
                                workoutGoal = it.workoutGoal.copy(value = result.data.goal.orEmpty()),
                                estimatedDuration = if (result.data.estimatedDurationMinutes == null) {
                                    it.estimatedDuration.copy(
                                        value = ""
                                    )
                                } else {
                                    it.estimatedDuration.copy(value = result.data.estimatedDurationMinutes.toString())
                                },
                                requiredEquipment = it.requiredEquipment.copy(value = result.data.requiredEquipment.orEmpty()),
                                masterNotes = it.masterNotes.copy(value = result.data.masterNotes.orEmpty()),
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateWorkoutName(workoutName: String) {
        _uiState.update {
            it.copy(
                workoutName = it.workoutName.copy(
                    value = workoutName
                )
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                description = it.description.copy(
                    value = description
                )
            )
        }
    }

    fun updateWorkoutGoal(goal: String) {
        _uiState.update {
            it.copy(
                workoutGoal = it.workoutGoal.copy(
                    value = goal
                )
            )
        }
    }

    fun updateEstimatedDuration(duration: String) {
        if (duration.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    estimatedDuration = it.estimatedDuration.copy(
                        value = duration
                    )
                )
            }
        }
    }

    fun updateRequiredEquipment(requiredEquipment: String) {
        _uiState.update {
            it.copy(
                requiredEquipment = it.requiredEquipment.copy(
                    value = requiredEquipment
                )
            )
        }
    }

    fun updateMasterNotes(masterNotes: String) {
        _uiState.update {
            it.copy(
                masterNotes = it.masterNotes.copy(
                    value = masterNotes
                )
            )
        }
    }

    fun saveWorkout(exercises: List<WorkoutExercise>) {
        setLoading(true)

        viewModelScope.launch {
            workoutSaveUseCase.execute(
                WorkoutSaveInput(
                    id = workoutId,
                    name = uiState.value.workoutName,
                    description = uiState.value.description,
                    workoutGoal = uiState.value.workoutGoal,
                    estimatedDuration = uiState.value.estimatedDuration,
                    requiredEquipment = uiState.value.requiredEquipment,
                    masterNotes = uiState.value.masterNotes,
                    exercises = exercises,
                    chickenId = arguments.chickenId,
                    chickenPath = arguments.chickenPath,
                    workoutPath = arguments.workoutPath
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
                        result.errorFormFields.forEach { formField ->
                            when (formField.field) {
                                WORKOUT_NAME -> _uiState.update { it.copy(workoutName = formField) }
                                DESCRIPTION -> _uiState.update { it.copy(description = formField) }
                                WORKOUT_GOAL -> _uiState.update { it.copy(workoutGoal = formField) }
                                ESTIMATED_DURATION -> _uiState.update { it.copy(estimatedDuration = formField) }
                                REQUIRED_EQUIPMENT -> _uiState.update { it.copy(requiredEquipment = formField) }
                                MASTER_NOTES -> _uiState.update { it.copy(masterNotes = formField) }
                                else -> {  }
                            }
                        }
                    }

                    is FormResource.Success<*> -> sendAction(UIAction.ShowSnackbar("Treino criado com sucesso"))
                }
            }
        }

        setLoading(false)
    }

    fun addExercise() {
        navigator.navigate(WorkoutRoute.WorkoutExerciseList)
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
            )
        }
    }
}
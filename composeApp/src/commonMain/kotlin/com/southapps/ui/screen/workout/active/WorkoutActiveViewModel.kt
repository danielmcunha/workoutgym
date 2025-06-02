package com.southapps.ui.screen.workout.active

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.workout.entities.WorkoutCompleted
import com.southapps.domain.workout.entities.WorkoutExerciseProgress
import com.southapps.domain.workout.entities.WorkoutProgress
import com.southapps.domain.workout.useCases.WorkoutCompleteUseCase
import com.southapps.domain.workout.useCases.WorkoutGetCurrentUseCase
import com.southapps.domain.workout.useCases.WorkoutSaveCurrentUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutActiveViewState(
    val workout: WorkoutProgress,
    val currentExercise: WorkoutExerciseProgress = workout.exercises.first(),
) : UIState {

    fun nextExercise(): WorkoutExerciseProgress? {
        val nextExerciseIndex = workout.exercises.indexOf(currentExercise) + 1
        if (nextExerciseIndex < workout.exercises.size) {
            return workout.exercises[nextExerciseIndex]
        }

        return null
    }

    val nextExerciseName: String?
        get() = nextExercise()?.name

    val exerciseStatePosition: String
        get() = "${workout.exercises.indexOf(currentExercise) + 1} de ${workout.exercises.size}"
}

class WorkoutActiveViewModel(
    navigator: Navigator,
    private val workoutCompleteUseCase: WorkoutCompleteUseCase,
    private val workoutSaveCurrentUseCase: WorkoutSaveCurrentUseCase,
    private val workoutGetCurrentUseCase: WorkoutGetCurrentUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WorkoutActiveViewState, UIAction>(navigator) {

    private val arguments = savedStateHandle.toRoute<WorkoutRoute.Active>()

    private var _uiState =
        MutableStateFlow(WorkoutActiveViewState(arguments.workoutProgress))
    override val uiState: StateFlow<WorkoutActiveViewState> = _uiState

    fun onExerciseSelected(workoutExercise: WorkoutExerciseProgress) {
        _uiState.update {
            it.copy(
                currentExercise = workoutExercise
            )
        }
    }

    fun nextExercise() {
        val next = uiState.value.nextExercise()
        if (next != null) {
            _uiState.update {
                it.copy(
                    currentExercise = next
                )
            }
        } else {
            completeWorkout()
        }
    }

    fun onSerieStateChanged(index: Int, state: Boolean) {
        val current = _uiState.value.currentExercise

        val newCurrent = current.copy(
            name = current.name,
            series = current.series.mapIndexed { i, serie ->
                val shouldUpdate = (state && i <= index) || (!state && i >= index)
                if (shouldUpdate) serie.copy(done = state) else serie
            }
        )

        _uiState.update {
            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises.map { exercise ->
                        if (exercise == current) newCurrent else exercise
                    }
                ),
                currentExercise = newCurrent
            )
        }

        viewModelScope.launch {
            workoutSaveCurrentUseCase.execute(
                _uiState.value.workout
            ).collect()
        }
    }

    fun completeWorkout() {
        sendAction(
            UIAction.ShowDialog(
                "Encerrar treino",
                "Você tem certeza que gostaria de encerrsar seu treino?",
                cancelButton = Pair("Cancelar") { },
                confirmButton = Pair("Sim") { saveAndFinish() }
            ))
    }

    fun checkCurrentWorkout() {
        viewModelScope.launch {
            delay(100)

            workoutGetCurrentUseCase.execute(Unit).collect { result ->
                when (result) {
                    is Resource.Error<*> -> TODO()
                    is Resource.Success<*> -> {
                        val currentWorkoutProgress = result.data as? WorkoutProgress
                        if (currentWorkoutProgress == null || currentWorkoutProgress?.name == _uiState.value.workout.name) return@collect

                        sendAction(
                            UIAction.ShowDialog(
                                "Treino em andamento",
                                "Você já possuí um treino em andamento ${currentWorkoutProgress.name}, deseja continuar?",
                                Pair("Não") {
                                    viewModelScope.launch {
                                        workoutSaveCurrentUseCase
                                            .execute(_uiState.value.workout)
                                            .collect()
                                    }
                                },
                                Pair("Sim") {
                                    _uiState.update {
                                        it.copy(
                                            workout = currentWorkoutProgress,
                                            currentExercise = currentWorkoutProgress.exercises.first()
                                        )
                                    }
                                }
                            ))
                    }
                }
            }
        }
    }

    private fun saveAndFinish() {
        viewModelScope.launch {
            val workout = uiState.value.workout.copy(
                endTime = currentTime().timeInMillis()
            )

            workoutCompleteUseCase.execute(workout).collect { result ->
                when (result) {
                    is Resource.Error<*> -> {}
                    is Resource.Success<WorkoutCompleted> -> navigator.navigate(
                        WorkoutRoute.Completed(
                            name = result.data.name,
                            duration = result.data.duration,
                            exercisesCompleted = result.data.exercisesCompleted,
                            exercises = result.data.exercises,
                            sequenceCount = result.data.sequenceCount
                        ), popBack = true
                    )
                }
            }
        }
    }
}
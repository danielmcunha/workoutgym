package com.southapps.ui.screen.workout

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.domain.workout.entities.WorkoutExerciseSeries
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SharedWorkoutUiState(
    val selectedExercises: List<WorkoutExercise> = emptyList()
) : UIState

class SharedWorkoutViewModel(
    navigator: Navigator
) : BaseViewModel<SharedWorkoutUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(SharedWorkoutUiState())
    override val uiState: StateFlow<SharedWorkoutUiState> = _uiState

    fun updateExercises(exercises: List<WorkoutExercise>) {
        _uiState.update {
            it.copy(selectedExercises = exercises)
        }
    }

    fun addExercise(workoutExercise: WorkoutExercise) {
        _uiState.value = _uiState.value.copy(
            selectedExercises = _uiState.value.selectedExercises + workoutExercise
        )
    }

    fun updateExerciseSeries(index: Int, series: Int) {
        _uiState.update { currentState ->
            val exercises = currentState.selectedExercises.toMutableList()
            val exercise = exercises[index]

            val reps = exercise.series.firstOrNull()?.name ?: ""

            val updatedSeries = List(series) {
                WorkoutExerciseSeries(name = reps)
            }

            exercises[index] = exercise.copy(series = updatedSeries)

            currentState.copy(selectedExercises = exercises)
        }
    }

    fun updateExerciseReps(index: Int, reps: Int) {
        _uiState.update { currentState ->
            val exercises = currentState.selectedExercises.toMutableList()
            val exercise = exercises[index]

            val updatedSeries = exercise.series.map {
                it.copy(name = reps.toString())
            }

            exercises[index] = exercise.copy(series = updatedSeries)

            currentState.copy(selectedExercises = exercises)
        }
    }

    fun updateExerciseOrder(updatedList: List<WorkoutExercise>) {
        _uiState.update { it.copy(selectedExercises = updatedList) }
    }
}
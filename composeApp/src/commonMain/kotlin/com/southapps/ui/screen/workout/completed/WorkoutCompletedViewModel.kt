package com.southapps.ui.screen.workout.completed

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.workout.entities.WorkoutCompleted
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class WorkoutSuccessViewState(
    val workoutCompleted: WorkoutCompleted,
) : UIState

class WorkoutSuccessViewModel(
    navigator: Navigator,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WorkoutSuccessViewState, UIAction>(navigator) {

    private val workoutCompleted = with(savedStateHandle.toRoute<WorkoutRoute.Completed>()) {
        WorkoutCompleted(
            name = this.name,
            duration = this.duration,
            exercisesCompleted = this.exercisesCompleted,
            exercises = this.exercises,
            sequenceCount = this.sequenceCount
        )
    }

    private val _uiState = MutableStateFlow(WorkoutSuccessViewState(workoutCompleted))
    override val uiState: StateFlow<WorkoutSuccessViewState> = _uiState

    fun share() {
        // todo: share
    }

    fun finish() {
        navigator.navigate(BottomNavigationRoute.Home)
    }
}
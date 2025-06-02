package com.southapps.ui.screen.workout.exercise.list

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.UserSession
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow

data class WorkoutExerciseListUiState(
    val isLoading: Boolean = false,
) : UIState

class WorkoutExerciseListViewModel(
    navigator: Navigator,
    userSession: UserSession
) : BaseViewModel<WorkoutExerciseListUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(WorkoutExerciseListUiState())
    override val uiState = _uiState
    private val allExercises: MutableList<WorkoutExercise> = mutableListOf()

    init {
        allExercises.addAll(userSession.getAllWorkoutExercises())
    }

    fun onAddExercises() {
        navigator.navigate(WorkoutRoute.WorkoutAddExercise)
    }

    fun onSave() {
        navigator.navigateBack()
    }
}
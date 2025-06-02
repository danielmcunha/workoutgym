package com.southapps.core

import com.southapps.domain.workout.entities.WorkoutExercise

sealed class UIAction {

    data class ShowSnackbar(val message: String) : UIAction()
    data class ShowDialog(
        val title: String,
        val message: String,
        val cancelButton: Pair<String, () -> Unit>,
        val confirmButton: Pair<String, () -> Unit>) : UIAction()
}

sealed class WorkoutCreateUiAction: UIAction() {

    data class UpdateExercises(val exercises: List<WorkoutExercise>): UIAction()
}
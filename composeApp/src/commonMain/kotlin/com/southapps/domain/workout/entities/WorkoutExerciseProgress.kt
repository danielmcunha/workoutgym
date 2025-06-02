package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExerciseProgress(
    val name: String,
    val category: String,
    val note: String?,
    val series: List<WorkoutExerciseSeriesProgress>,
) {
    val done: Boolean
        get() = series.all { it.done }
}
package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExerciseSeriesProgress(
    val name: String,
    val value: String? = null,
    val done: Boolean = false
)
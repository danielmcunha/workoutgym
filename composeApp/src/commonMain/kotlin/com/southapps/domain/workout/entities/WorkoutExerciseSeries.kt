package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExerciseSeries(
    val name: String,
    val value: String? = null
)
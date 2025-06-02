package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSummary(
    val workoutReference: String,
    val order: Int,
    val name: String,
    val workoutGoal: String? = null,
)
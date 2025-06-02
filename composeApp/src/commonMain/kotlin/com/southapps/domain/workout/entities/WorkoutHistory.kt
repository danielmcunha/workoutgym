package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutHistory(
    val name: String,
    val workouts: List<WorkoutProgress>
)
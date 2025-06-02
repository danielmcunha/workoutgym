package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutCompleted(
    val name: String,
    val duration: Long,
    val exercisesCompleted: Int,
    val exercises: Int,
    val sequenceCount: Int
)
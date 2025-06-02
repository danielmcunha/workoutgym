package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class WorkoutExercise @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val name: String,
    val category: String,
    val muscleGroup: MuscleGroup,
    val series: List<WorkoutExerciseSeries>,
    val note: String? = null,
    val order: Int = 0
)
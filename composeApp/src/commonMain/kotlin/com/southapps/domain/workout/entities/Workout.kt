package com.southapps.domain.workout.entities

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Workout(
    val uid: String = Uuid.random().toString(),
    val name: String,
    val order: Int,
    val exercises: List<WorkoutExercise> = emptyList(),
    val isActive: Boolean = false,
    val masterId: String? = null,
    val description: String? = null,
    val goal: String? = null,
    val estimatedDurationMinutes: Int? = null,
    val requiredEquipment: String? = null,
    val masterNotes: String? = null,
)

//todo remove
fun fakeWorkout() = Workout(
    name = "Treino de teste",
    order = 0,
    exercises = listOf(
        WorkoutExercise(
            name = "Rosca direta",
            category = "Biceps",
            order = 0,
            note = null,
            muscleGroup = MuscleGroup.BICEPS,
            series = listOf(
                WorkoutExerciseSeries(
                    name = "20",
                    value = "6"
                ),
                WorkoutExerciseSeries(
                    name = "10",
                    value = "10"
                ),
                WorkoutExerciseSeries(
                    name = "10",
                    value = "14"
                ),
                WorkoutExerciseSeries(
                    name = "8",
                    value = "16"
                )
            )
        ),
        WorkoutExercise(
            name = "Rosca scott",
            category = "Biceps",
            order = 1,
            note = "barra 5 kg lado",
            muscleGroup = MuscleGroup.BICEPS,
            series = listOf(
                WorkoutExerciseSeries(
                    name = "10",
                    value = null
                ),
                WorkoutExerciseSeries(
                    name = "10",
                    value = null
                ),
                WorkoutExerciseSeries(
                    name = "10",
                    value = null
                )
            )
        ),
        WorkoutExercise(
            name = "Rosca martelo",
            category = "Biceps",
            order = 2,
            note = "14 kg",
            muscleGroup = MuscleGroup.BICEPS,
            series = listOf(
                WorkoutExerciseSeries(
                    name = "10",
                ),
                WorkoutExerciseSeries(
                    name = "10",
                ),
                WorkoutExerciseSeries(
                    name = "10",
                ),
                WorkoutExerciseSeries(
                    name = "10",
                )
            )
        ),
        WorkoutExercise(
            name = "Treino de tiro",
            category = "Cardio",
            order = 3,
            note = "30 minutos",
            muscleGroup = MuscleGroup.CARDIO,
            series = listOf(
                WorkoutExerciseSeries(
                    name = "5",
                ),
                WorkoutExerciseSeries(
                    name = "5",
                ),
                WorkoutExerciseSeries(
                    name = "5",
                ),
                WorkoutExerciseSeries(
                    name = "5",
                ),
                WorkoutExerciseSeries(
                    name = "10",
                ),
            )
        )
    ),
    isActive = true,
    masterId = null
)
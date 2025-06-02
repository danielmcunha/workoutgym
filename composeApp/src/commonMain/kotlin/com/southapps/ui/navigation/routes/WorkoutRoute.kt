package com.southapps.ui.navigation.routes

import com.southapps.core.extensions.fromJson
import com.southapps.core.extensions.toJson
import com.southapps.domain.workout.entities.WorkoutProgress
import kotlinx.serialization.Serializable

object WorkoutRoute {

    @Serializable
    object Workout

    @Serializable
    data class WorkoutCreate(
        val chickenId: String? = null,
        val chickenPath: String? = null,
        val workoutPath: String? = null
    )

    @Serializable
    object WorkoutExerciseList

    @Serializable
    object WorkoutAddExercise

    @Serializable
    data class RoutineManager(
        val userName: String,
        val chickenPath: String?,
        val workoutsJson: String?
    )

    @Serializable
    data class RoutineCreate(
        val chickenPath: String?,
        val workoutsJson: String?,
        val routineJson: String? = null
    )

    @Serializable
    data class Active(
        val data: String,
    ) {
        constructor(
            workoutProgress: WorkoutProgress,
        ) : this(workoutProgress.toJson())

        val workoutProgress: WorkoutProgress
            get() = data.fromJson<WorkoutProgress>()
    }

    @Serializable
    object Report

    @Serializable
    data class Completed(
        val name: String,
        val duration: Long,
        val exercisesCompleted: Int,
        val exercises: Int,
        val sequenceCount: Int,
    )
}
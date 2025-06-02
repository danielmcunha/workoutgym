package com.southapps.domain.workout.entities

import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.formatTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.core.extensions.toShortDate
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class WorkoutProgress @OptIn(ExperimentalUuidApi::class) constructor(
    val name: String,
    val exercises: List<WorkoutExerciseProgress>,
    val startTime: Long,
    val endTime: Long? = null,
    val effortLevel: Float? = null,
    val id: String = Uuid.random().toString()
) {
    val progress: Double
        get() {
            if (exercises.isEmpty()) return 0.0

            return exercises.count { it.done } / exercises.size.toDouble()
        }

    val duration: String
        get() = (endTime ?: startTime).minus(startTime).formatTime()

    val shortDate: String
        get() = startTime.toShortDate()

    val exercisesCompletion: String
        get() = "${exercises.count { it.done }} de ${exercises.size}"

    companion object {
        fun fromWorkout(workout: Workout): WorkoutProgress {
            return WorkoutProgress(
                name = workout.name,
                exercises = workout.exercises.map { exercise ->
                    WorkoutExerciseProgress(
                        name = exercise.name,
                        category = exercise.category,
                        note = exercise.note,
                        series = exercise.series.map { serie ->
                            WorkoutExerciseSeriesProgress(
                                name = serie.name,
                                value = serie.value
                            )
                        }
                    )
                },
                startTime = currentTime().timeInMillis()
            )
        }
    }
}
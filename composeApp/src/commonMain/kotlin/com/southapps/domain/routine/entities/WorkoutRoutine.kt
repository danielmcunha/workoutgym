package com.southapps.domain.routine.entities

import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.workout.entities.WorkoutLocationType
import com.southapps.domain.workout.entities.WorkoutSummary
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRoutine(
    val frequencyType: FrequencyType,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val scheduledTime: Long,
    val isPresential: Boolean,
    val locationType: WorkoutLocationType?,
    val locationDetails: String?,
    val workoutSummary: WorkoutSummary?,
    val timesPerWeek: Int? = null,
    val masterId: String? = null,
    val uid: String? = null
)

val WorkoutRoutine.formattedScheduledTime: String
    get() {
        val timeStr = scheduledTime.toString().padStart(4, '0')
        val hours = timeStr.substring(0, 2)
        val minutes = timeStr.substring(2, 4)
        return "$hours:$minutes"
    }
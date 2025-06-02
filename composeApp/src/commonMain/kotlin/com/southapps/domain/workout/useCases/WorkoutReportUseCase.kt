package com.southapps.domain.workout.useCases

import com.southapps.core.extensions.formatTime
import com.southapps.core.extensions.toDateLiteral
import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.workout.entities.WorkoutReport

class WorkoutReportUseCase(
    private val workoutRepository: WorkoutRepository,
) : UseCase<String, WorkoutReport>() {
    override suspend fun implementation(input: String): WorkoutReport {
        val history = workoutRepository.getHistory(input)

        val workouts = history?.workouts ?: listOf()
        val completionAverage = (workouts.sumOf { it.progress } / workouts.size * 100).toInt()
        val lastWorkout = workouts.lastOrNull()

        val lastWorkoutDuration =
            lastWorkout?.endTime?.minus(lastWorkout.startTime)?.formatTime() ?: "N/A"
        val lastWorkoutDate = lastWorkout?.startTime?.toDateLiteral() ?: "N/A"

        return WorkoutReport(
            workoutName = input,
            workouts = workouts,
            completionAverage = completionAverage,
            sequenceCount = workouts.size,
            lastWorkoutDuration = lastWorkoutDuration,
            lastWorkoutDate = lastWorkoutDate
        )
    }
}
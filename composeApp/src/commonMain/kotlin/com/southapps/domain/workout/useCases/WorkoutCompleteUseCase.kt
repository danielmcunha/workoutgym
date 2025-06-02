package com.southapps.domain.workout.useCases

import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.workout.entities.WorkoutCompleted
import com.southapps.domain.workout.entities.WorkoutHistory
import com.southapps.domain.workout.entities.WorkoutProgress

class WorkoutCompleteUseCase(
    private val workoutRepository: WorkoutRepository,
) : UseCase<WorkoutProgress, WorkoutCompleted>() {
    override suspend fun implementation(input: WorkoutProgress): WorkoutCompleted {
        val savedHistory = workoutRepository.getHistory(input.name)

        val newHistory = savedHistory?.copy(
            workouts = savedHistory.workouts + input
        ) ?: WorkoutHistory(
            name = input.name,
            workouts = listOf(input)
        )

        workoutRepository.deleteCurrentWorkout(input.id)
        workoutRepository.saveHistory(newHistory)

        return WorkoutCompleted(
            name = input.name,
            duration = input.endTime?.minus(input.startTime) ?: 0,
            exercisesCompleted = input.exercises.count { it.done },
            exercises = input.exercises.size,
            sequenceCount = newHistory.workouts.count()
        )
    }
}
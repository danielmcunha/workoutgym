package com.southapps.domain.workout.useCases

import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.workout.entities.WorkoutProgress

class WorkoutSaveCurrentUseCase(
    private val workoutRepository: WorkoutRepository,
) : UseCase<WorkoutProgress, Unit>() {
    override suspend fun implementation(input: WorkoutProgress) {
        workoutRepository.saveCurrentWorkout(input)
    }
}
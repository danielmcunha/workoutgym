package com.southapps.domain.workout.useCases

import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase

class WorkoutDeleteCurrentUseCase(
    private val workoutRepository: WorkoutRepository
): UseCase<String, Unit>() {
    override suspend fun implementation(input: String) {
        workoutRepository.deleteCurrentWorkout(input)
    }
}
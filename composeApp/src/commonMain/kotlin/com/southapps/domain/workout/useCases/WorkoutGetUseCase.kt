package com.southapps.domain.workout.useCases

import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.workout.entities.Workout

class WorkoutGetUseCase(
    private val repository: WorkoutRepository,
) : UseCase<String, Workout>() {

    override suspend fun implementation(input: String): Workout {
        return repository.getWorkoutFromPath(input)
    }
}
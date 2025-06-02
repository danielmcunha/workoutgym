package com.southapps.domain.workout.useCases

import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.workout.entities.WorkoutProgress

class WorkoutGetCurrentUseCase(
    private val workoutRepository: WorkoutRepository
) : UseCase<Unit, WorkoutProgress?>(){
    override suspend fun implementation(input: Unit): WorkoutProgress? {
        return workoutRepository.getCurrentWorkout()
    }
}
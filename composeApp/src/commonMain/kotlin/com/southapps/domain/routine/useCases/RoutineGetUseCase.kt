package com.southapps.domain.routine.useCases

import com.southapps.data.routine.RoutineRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.routine.entities.WorkoutRoutine

class RoutineGetUseCase(
    private val repository: RoutineRepository,
) : UseCase<String, List<WorkoutRoutine>>() {

    override suspend fun implementation(input: String): List<WorkoutRoutine> {
        return repository.getRoutine(input)
    }
}
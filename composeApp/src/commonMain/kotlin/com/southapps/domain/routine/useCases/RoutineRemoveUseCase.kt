package com.southapps.domain.routine.useCases

import com.southapps.data.routine.RoutineRepository
import com.southapps.domain.common.useCase.UseCase

data class RoutineRemoveInput(
    val chickenPath: String?,
    val routineId: String
)

class RoutineRemoveUseCase(
    private val repository: RoutineRepository
) : UseCase<RoutineRemoveInput, Unit>() {

    override suspend fun implementation(input: RoutineRemoveInput) {
        return repository.removeRoutine(input.chickenPath, input.routineId)
    }
}
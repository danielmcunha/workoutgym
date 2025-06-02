package com.southapps.domain.workout.useCases

import com.southapps.data.session.UserSession
import com.southapps.data.workout.WorkoutRepository
import com.southapps.domain.common.useCase.FormUseCase
import com.southapps.domain.workout.entities.Workout
import com.southapps.domain.workout.entities.WorkoutSaveInput
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class WorkoutSaveUseCase(private val repository: WorkoutRepository, private val userSession: UserSession) : FormUseCase<WorkoutSaveInput, Unit>() {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun implementation(input: WorkoutSaveInput) {
        validate(
            input.name,
            input.description,
            input.workoutGoal,
            input.estimatedDuration,
            input.requiredEquipment,
            input.masterNotes,
        )

        val workout = Workout(
            uid = input.id ?: Uuid.random().toString(),
            name = input.name.value.toString(),
            description = input.description.value.toString(),
            goal = input.workoutGoal.value.toString(),
            estimatedDurationMinutes = input.estimatedDuration.value?.toString()?.toInt(),
            requiredEquipment = input.requiredEquipment.value.toString(),
            masterNotes = input.masterNotes.value.toString(),
            order = 0,
            exercises = input.exercises,
            masterId = if (input.chickenPath != null) userSession.currentUser.value?.uid else null
        ).run {
            if (input.chickenPath != null) {
                this.copy(masterId = userSession.currentUser.value?.uid.orEmpty())
            } else {
                this
            }
        }

        repository.createOrEdit(
            workout = workout,
            chickenId = input.chickenId
        )
    }
}
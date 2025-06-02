package com.southapps.domain.routine.useCases

import com.southapps.data.routine.RoutineRepository
import com.southapps.data.session.UserSession
import com.southapps.domain.common.useCase.FormUseCase
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.routine.entities.WorkoutRoutine
import com.southapps.domain.routine.validation.RoutineFormValidationUtils
import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.workout.entities.WorkoutLocationType
import com.southapps.domain.workout.entities.WorkoutSummary
import kotlinx.datetime.DayOfWeek

data class RoutineCreateInput(
    val chickenPath: String?,
    val uid: String?,
    val frequencyType: FormField,
    val daysOfWeek: FormField,
    val timesPerWeek: FormField,
    val isPresential: FormField,
    val scheduledTime: FormField,
    val locationType: FormField,
    val locationDetails: FormField,
    val workoutSummary: FormField
)

class RoutineCreateUseCase(
    private val repository: RoutineRepository,
    private val userSession: UserSession
) : FormUseCase<RoutineCreateInput, Unit>() {

    override suspend fun implementation(input: RoutineCreateInput) {
        validate(
            input.workoutSummary.copy(
                customValidator = {
                    RoutineFormValidationUtils.workoutValidator(it)
                }
            ),
            input.frequencyType.copy(
                customValidator = {
                    RoutineFormValidationUtils.frequencyValidator(
                        it,
                        daysOfWeek = input.daysOfWeek.value as? Set<DayOfWeek>,
                        timesPerWeek = input.timesPerWeek.value as Int
                    )
                },
            ),
            input.daysOfWeek,
            input.timesPerWeek,
            input.scheduledTime.copy(
                customValidator = {
                    RoutineFormValidationUtils.scheduledTimeValidator(it)
                }
            ),
            input.locationType.copy(
                customValidator = {
                    RoutineFormValidationUtils.locationTypeValidator(it)
                }
            ),
            input.locationDetails
        )

        repository.createRoutine(
            chickenPath = input.chickenPath,
            routine = WorkoutRoutine(
                uid = input.uid,
                masterId = userSession.currentUser.value?.uid.orEmpty(),
                frequencyType = input.frequencyType.value as FrequencyType,
                daysOfWeek = input.daysOfWeek.value as Set<DayOfWeek>,
                timesPerWeek = input.timesPerWeek.value as Int,
                isPresential = input.isPresential.value as Boolean,
                scheduledTime = (input.scheduledTime.value as String).toLongOrNull() ?: 0L,
                locationType = input.locationType.value as? WorkoutLocationType,
                locationDetails = input.locationDetails.value as String,
                workoutSummary = input.workoutSummary.value as? WorkoutSummary
            )
        )
    }
}
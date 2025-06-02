package com.southapps.domain.routine.validation

import com.southapps.domain.common.validation.ValidationError
import com.southapps.domain.common.validation.ValidationResult
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.workout.entities.FrequencyType

object RoutineFormValidationUtils {

    fun scheduledTimeValidator(formField: FormField): ValidationResult? {
        val time = formField.value?.toString()?.toLongOrNull()
            ?: return ValidationError.InvalidTime

        val length = time.toString().length
        val hours = time / 100
        val minutes = time % 100

        if (length !in 3..4 || hours !in 0..23 || minutes !in 0..59) {
            return ValidationError.InvalidTime
        }

        return null
    }

    fun frequencyValidator(
        formField: FormField,
        daysOfWeek: Set<*>?,
        timesPerWeek: Int
    ): ValidationResult? {
        val frequencyType = formField.value as? FrequencyType

        val relatedDays = daysOfWeek ?: emptySet<Any>()
        val relatedTimes = timesPerWeek as? Int ?: 0

        when (frequencyType) {
            FrequencyType.DAYS_OF_WEEK -> {
                if (relatedDays.isEmpty()) return ValidationError.MissingDays
            }

            FrequencyType.TIMES_PER_WEEK -> {
                if (relatedTimes !in 1..7) return ValidationError.InvalidFrequency
            }

            null -> {
                return ValidationError.InvalidFrequency
            }
        }

        return null
    }

    fun workoutValidator(formField: FormField): ValidationResult? {
        if (formField.value == null) return ValidationError.MissingWorkout

        return null
    }

    fun locationTypeValidator(formField: FormField): ValidationResult? {
        if (formField.value == null) return ValidationError.MissingLocation

        return null
    }
}
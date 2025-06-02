package com.southapps.domain.workout.entities

import com.southapps.domain.common.validation.form.FormField


data class WorkoutSaveInput(
    val id: String?,
    val name: FormField,
    val description: FormField,
    val workoutGoal: FormField,
    val estimatedDuration: FormField,
    val requiredEquipment: FormField,
    val masterNotes: FormField,
    val exercises: List<WorkoutExercise>,
    val chickenId: String?,
    val chickenPath: String?,
    val workoutPath: String?
)
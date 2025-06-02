package com.southapps.ui.util.extension

import com.southapps.domain.workout.entities.WorkoutLocationType


fun WorkoutLocationType.getText(): String {
    return when (this) {
        WorkoutLocationType.STUDENT_GYM -> "Academia do aluno"
        WorkoutLocationType.PERSONAL_GYM -> "Academia do personal"
        WorkoutLocationType.STUDENT_HOME -> "Residência do aluno"
        WorkoutLocationType.ONLINE_CALL -> "Online"
        WorkoutLocationType.PARK_OUTDOOR -> "Parque"
        WorkoutLocationType.OTHER -> "Outro"
    }
}
package com.southapps.ui.util.extension

import com.southapps.domain.workout.entities.FrequencyType

fun FrequencyType.getText(): String {
    return when (this) {
        FrequencyType.DAYS_OF_WEEK -> "Dias da semana"
        FrequencyType.TIMES_PER_WEEK -> "Vezes por semana"
    }
}
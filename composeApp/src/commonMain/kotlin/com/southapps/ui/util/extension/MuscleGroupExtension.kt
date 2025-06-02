package com.southapps.ui.util.extension

import com.southapps.domain.workout.entities.MuscleGroup

fun MuscleGroup.getText(): String {
    return when (this) {
        MuscleGroup.CHEST -> "Peito"
        MuscleGroup.BACK -> "Costas"
        MuscleGroup.LEG -> "Perna"
        MuscleGroup.SHOULDER -> "Ombro"
        MuscleGroup.BICEPS -> "Bíceps"
        MuscleGroup.TRICEPS -> "Tríceps"
        MuscleGroup.ABS -> "Abdomên"
        MuscleGroup.GLUTE -> "Glúteo"
        MuscleGroup.CALVE -> "Panturrilha"
        MuscleGroup.FOREARM -> "Antebraço"
        MuscleGroup.CARDIO -> "Cardio"
    }
}
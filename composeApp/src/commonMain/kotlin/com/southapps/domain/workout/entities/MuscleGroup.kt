package com.southapps.domain.workout.entities

import kotlinx.serialization.Serializable

@Serializable
enum class MuscleGroup {
    CHEST,
    BACK,
    LEG,
    SHOULDER,
    BICEPS,
    TRICEPS,
    ABS,
    GLUTE,
    CALVE,
    FOREARM,
    CARDIO
}
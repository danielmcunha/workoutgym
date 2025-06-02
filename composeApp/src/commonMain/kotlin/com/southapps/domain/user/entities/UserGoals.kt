package com.southapps.domain.user.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserGoals(
    val mainGoal:String,
    val experience: String,
    val weeklyFrequency: String
)
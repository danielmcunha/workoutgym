package com.southapps.domain.user.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserBodyInfo(
    val height: String,
    val weight: String
)
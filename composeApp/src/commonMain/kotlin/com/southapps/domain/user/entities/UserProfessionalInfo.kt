package com.southapps.domain.user.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserProfessionalInfo(
    val name: String,
    val description: String? = null,
    val phone: String? = null,
    val instagram: String? = null,
    val facebook: String? = null,
    val tiktok: String? = null,
    val youtube: String? = null,
)
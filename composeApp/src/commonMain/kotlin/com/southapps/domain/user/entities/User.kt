package com.southapps.domain.user.entities

import com.southapps.domain.master.entities.MasterSummary
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val type: UserType? = null,
    val bornDate: String? = null,
    val gender: String? = null,
    val bodyInfo: UserBodyInfo? = null,
    val goals: UserGoals? = null,
    val professionalInfo: UserProfessionalInfo? = null,
    val masterSummary: MasterSummary? = null,
    val createdAt: Long? = null,
)
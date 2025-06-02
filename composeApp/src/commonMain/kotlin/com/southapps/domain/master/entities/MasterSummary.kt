package com.southapps.domain.master.entities

import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.domain.user.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class MasterSummary(
    val masterId: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val createdAt: Long = currentTime().timeInMillis(),
)

fun User.toMasterSummary() = MasterSummary(
    masterId = this.uid.orEmpty(),
    name = this.professionalInfo?.name ?: this.name.orEmpty(),
    email = this.email.orEmpty(),
    phone = this.professionalInfo?.phone ?: this.phone
)

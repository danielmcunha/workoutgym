package com.southapps.domain.associate.entities

import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.domain.master.entities.MasterSummary
import kotlinx.serialization.Serializable

@Serializable
data class AssociateInvite(
    val masterSummary: MasterSummary,
    val chickenEmail: String,
    val accepted: Boolean? = null,
    val inviteCreatedAt: Long = currentTime().timeInMillis(),
    val id: String? = null,
)
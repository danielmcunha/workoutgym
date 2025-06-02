package com.southapps.domain.associate.entities

import com.southapps.domain.master.entities.MasterSummary
import com.southapps.domain.user.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class AssociateAcceptInvite(
    val user: User,
    val masterSummary: MasterSummary,
    val associateInvite: AssociateInvite
)
package com.southapps.data.associate

import com.southapps.domain.associate.entities.AssociateInvite
import dev.gitlive.firebase.firestore.Transaction

interface AssociateRepository {
    fun getMasterId() : String

    suspend fun createInvite(associateInvite: AssociateInvite)

    suspend fun checkOpenInvite(email: String) : AssociateInvite?

    suspend fun getInvites(email: String) : List<AssociateInvite>

    suspend fun deleteInvite(inviteId: String)

    suspend fun updateInvite(invite: AssociateInvite, transaction: Transaction? = null)

    suspend fun getOpenInvites(masterId: String) : List<AssociateInvite>
}
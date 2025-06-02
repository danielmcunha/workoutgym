package com.southapps.data.associate

import com.southapps.domain.associate.entities.AssociateInvite
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Transaction
import dev.gitlive.firebase.firestore.firestore

class AssociateRepositoryImpl : AssociateRepository {
    override fun getMasterId(): String {
        return Firebase.auth.currentUser?.uid ?: throw Exception("Invalid master ID")
    }

    override suspend fun createInvite(
        associateInvite: AssociateInvite,
    ) {
        try {
            val ref = Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .add(associateInvite)

            Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .document(ref.id)
                .set(
                    associateInvite.copy(
                        id = ref.id
                    )
                )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun checkOpenInvite(email: String): AssociateInvite? {
        try {
            return Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .where {
                    all(
                        "chickenEmail" equalTo email,
                        "accepted" equalTo null
                    )
                }
                .get()
                .documents
                .firstOrNull()
                ?.data()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getInvites(email: String): List<AssociateInvite> {
        try {
            return Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .where {
                    all(
                        "chickenEmail" equalTo email
                    )
                }
                .get()
                .documents
                .map { it.data(AssociateInvite.serializer()) }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteInvite(inviteId: String) {
        try {
            Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .document(inviteId)
                .delete()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateInvite(invite: AssociateInvite, transaction: Transaction?) {
        try {
            val docRef = Firebase.firestore
                .collection(ASSOCIATE_INVITES)
                .document(invite.id.orEmpty())

            val updateInternal: Transaction.() -> Unit = {
                this.update(docRef, invite)
            }

            transaction?.updateInternal() ?: Firebase.firestore.runTransaction {
                updateInternal()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getOpenInvites(masterId: String): List<AssociateInvite> {
        return Firebase.firestore
            .collection(ASSOCIATE_INVITES)
            .where {
                all(
                    "masterSummary.masterId" equalTo masterId,
                    "accepted" equalTo null
                )
            }
            .get()
            .documents
            .map { it.data(AssociateInvite.serializer()) }
    }

    companion object {
        private const val ASSOCIATE_INVITES = "associate_invites"
    }
}
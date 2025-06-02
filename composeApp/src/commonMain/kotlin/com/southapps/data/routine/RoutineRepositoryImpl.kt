package com.southapps.data.routine

import com.southapps.data.user.UserRepositoryImpl
import com.southapps.domain.routine.entities.WorkoutRoutine
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoutineRepositoryImpl : RoutineRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createRoutine(
        chickenPath: String?,
        routine: WorkoutRoutine
    ) {
        val currentUserId = Firebase.auth.currentUser?.uid.orEmpty()

        val routineId = routine.uid ?: Uuid.random().toString()
        val routineToSave = routine.copy(uid = routineId)

        try {
            Firebase.firestore.batch().apply {
                chickenPath?.let {
                    val chickenRef = Firebase.firestore
                        .document(chickenPath)
                        .collection(ROUTINES_COLLECTION_PATH)
                        .document(routineId)

                    this.set(chickenRef, routineToSave)
                }

                val userRef = Firebase.firestore
                    .collection(UserRepositoryImpl.COLLECTION_PATH)
                    .document(currentUserId)
                    .collection(ROUTINES_COLLECTION_PATH)
                    .document(routineId)

                this.set(userRef, routineToSave)

                this.commit()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getRoutine(userPath: String): List<WorkoutRoutine> {
        return try {
            Firebase.firestore
                .document(userPath)
                .collection(ROUTINES_COLLECTION_PATH)
                .get()
                .documents
                .map { doc ->
                    doc.data(WorkoutRoutine.serializer())
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun removeRoutine(chickenPath: String?, routineId: String) {
        val currentUserId = Firebase.auth.currentUser?.uid.orEmpty()

        try {
            Firebase.firestore.runTransaction {
                chickenPath?.let { path ->
                    val chickenRoutineRef = Firebase.firestore
                        .document(path)
                        .collection(ROUTINES_COLLECTION_PATH)
                        .document(routineId)

                    this.delete(chickenRoutineRef)

                    val userRoutineRef = Firebase.firestore
                        .collection(UserRepositoryImpl.COLLECTION_PATH)
                        .document(currentUserId)
                        .collection(ROUTINES_COLLECTION_PATH)
                        .document(routineId)

                    this.delete(userRoutineRef)
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    companion object {
        const val ROUTINES_COLLECTION_PATH = "routines"

        fun getRoutinePath(userPath: String, id: String) = "$userPath/$ROUTINES_COLLECTION_PATH/$id"
    }
}
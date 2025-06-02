package com.southapps.data.user

import com.southapps.data.AuthenticationException
import com.southapps.data.workout.WorkoutRepositoryImpl.Companion.CHICKENS_COLLECTION_PATH
import com.southapps.data.workout.WorkoutRepositoryImpl.Companion.WORKOUTS_COLLECTION_PATH
import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.domain.user.entities.User
import com.southapps.domain.workout.entities.WorkoutSummary
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Transaction
import dev.gitlive.firebase.firestore.firestore

class UserRepositoryImpl : UserRepository {

    override suspend fun signIn(email: String, password: String): User {
        val signInResult = Firebase.auth.signInWithEmailAndPassword(email, password).user

        return if (signInResult != null) {
            getUser(signInResult.uid)
        } else {
            throw Exception()
        }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun getUsers(): List<User> {
        val userResponse =
            Firebase.firestore.collection(COLLECTION_PATH).get()

        return userResponse.documents.map {
            it.data()
        }
    }

    override suspend fun getCurrentAuthenticatedUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun getUser(uid: String): User {
        return Firebase.firestore.collection(COLLECTION_PATH).document(uid).get().data()
    }

    override suspend fun registerWithEmailAndPassword(user: User, password: String): User {
        val authResult =
            Firebase.auth.createUserWithEmailAndPassword(user.email.orEmpty(), password)
        val firebaseUser: FirebaseUser = authResult.user
            ?: throw AuthenticationException("Criação de usuário Firebase falhou: usuário nulo após registro.")

        return user.copy(uid = firebaseUser.uid).apply {
            createUserFirestore(this)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
    }

    override suspend fun refreshToken() =
        Firebase.auth.currentUser?.let {
            true
        } ?: run {
            // TODO get saved user
            false
        }

    override suspend fun createUserFirestore(user: User) {
        user.uid?.let {
            Firebase.firestore
                .collection(COLLECTION_PATH)
                .document(it)
                .set(user)
        }
    }

    override suspend fun updateUser(user: User, transaction: Transaction?) {
        user.uid?.let {
            val docRef = Firebase.firestore
                .collection(COLLECTION_PATH)
                .document(it)

            val updateInternal: Transaction.() -> Unit = {
                this.update(docRef, user)
            }

            transaction?.updateInternal() ?: Firebase.firestore.runTransaction {
                updateInternal()
            }
        }
    }

    override suspend fun getChickens(): List<ChickenSummary> {
        return Firebase.auth.currentUser?.let { user ->
            val chickens = Firebase.firestore
                .collection(COLLECTION_PATH)
                .document(user.uid)
                .collection(CHICKENS_COLLECTION_PATH)
                .get()
                .documents
                .map { doc ->
                    val workouts = doc.reference
                        .collection(WORKOUTS_COLLECTION_PATH)
                        .get()
                        .documents
                        .map { it.data(WorkoutSummary.serializer()) }

                    doc.data(ChickenSummary.serializer()).copy(workouts = workouts)
                }

            return chickens
        } ?: emptyList()
    }

    override suspend fun addChicken(
        masterId: String,
        chickenSummary: ChickenSummary,
        transaction: Transaction?,
    ) {
        val docRef = Firebase.firestore
            .collection(COLLECTION_PATH)
            .document(masterId)
            .collection(CHICKENS_COLLECTION_PATH)
            .document(chickenSummary.userId)

        val setInternal: Transaction.() -> Unit = {
            this.set(docRef, chickenSummary)
        }

        transaction?.setInternal() ?: Firebase.firestore.runTransaction {
            setInternal()
        }
    }

    override suspend fun removeChicken(
        masterId: String,
        chickenId: String,
        transaction: Transaction?,
    ) {
        val docRef = Firebase.firestore
            .collection(COLLECTION_PATH)
            .document(masterId)
            .collection(CHICKENS_COLLECTION_PATH)
            .document(chickenId)

        val deleteInternal: Transaction.() -> Unit = {
            this.delete(docRef)
        }

        transaction?.deleteInternal() ?: Firebase.firestore.runTransaction {
            deleteInternal()
        }
    }

    companion object {
        const val COLLECTION_PATH = "users"
    }
}
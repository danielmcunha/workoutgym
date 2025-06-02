package com.southapps.data.user

import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.domain.user.entities.User
import dev.gitlive.firebase.firestore.Transaction

interface UserRepository {

    suspend fun signIn(email: String, password: String): User
    suspend fun signOut()
    suspend fun getCurrentAuthenticatedUserId(): String?
    suspend fun getUsers(): List<User>
    suspend fun getUser(uid: String): User
    suspend fun createUserFirestore(user: User)
    suspend fun updateUser(user: User, transaction: Transaction? = null)
    suspend fun getChickens(): List<ChickenSummary>
    suspend fun addChicken(masterId: String, chickenSummary: ChickenSummary, transaction: Transaction? = null)
    suspend fun removeChicken(masterId: String, chickenId: String, transaction: Transaction? = null)
    suspend fun registerWithEmailAndPassword(user: User, password: String): User

    suspend fun sendPasswordResetEmail(email: String)

    suspend fun refreshToken(): Boolean
}
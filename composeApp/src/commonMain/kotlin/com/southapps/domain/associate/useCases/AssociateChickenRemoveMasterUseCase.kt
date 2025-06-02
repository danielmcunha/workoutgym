package com.southapps.domain.associate.useCases

import com.southapps.data.session.MutableUserSession
import com.southapps.data.session.UserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.common.exception.GenericException
import com.southapps.domain.common.useCase.UseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class AssociateChickenRemoveMasterUseCase(
    private val userSession: UserSession,
    private val userRepository: UserRepository,
    private val mutableUserSession: MutableUserSession,
) : UseCase<Unit, Unit>() {
    override suspend fun implementation(input: Unit) {
        val user = userSession.currentUser.value ?: throw GenericException()
        val chickenId = user.uid ?: throw GenericException()
        val masterSummary = user.masterSummary ?: throw GenericException()

        val newUser = user.copy(
            masterSummary = null
        )

        Firebase.firestore.runTransaction {
            userRepository.removeChicken(masterSummary.masterId, chickenId, this@runTransaction)
            userRepository.updateUser(newUser, this@runTransaction)
            mutableUserSession.updateUser(newUser)
        }
    }
}

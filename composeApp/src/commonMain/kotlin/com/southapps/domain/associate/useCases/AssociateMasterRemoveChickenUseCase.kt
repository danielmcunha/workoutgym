package com.southapps.domain.associate.useCases

import com.southapps.data.session.UserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.common.exception.GenericException
import com.southapps.domain.common.useCase.UseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class AssociateMasterRemoveChickenUseCase(
    private val userSession: UserSession,
    private val userRepository: UserRepository,
) : UseCase<String, Unit>() {
    override suspend fun implementation(input: String) {
        val master =
            userSession.currentUser.value ?: throw GenericException()
        val masterId = master.uid ?: throw GenericException()

        Firebase.firestore.runTransaction {
            val chicken = userRepository.getUser(input)

            userRepository.updateUser(
                chicken.copy(masterSummary = null),
                this@runTransaction
            )

            userRepository.removeChicken(masterId, input, this@runTransaction)
        }
    }
}

package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.associate.entities.AssociateAcceptInvite
import com.southapps.domain.associate.validation.AssociateValidationUtils
import com.southapps.domain.chicken.entities.ChickenSummary.Companion.toChickenSummary
import com.southapps.domain.common.useCase.UseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class AssociateAcceptInviteUseCase(
    private val associateRepository: AssociateRepository,
    private val userRepository: UserRepository,
    private val mutableUserSession: MutableUserSession,
) : UseCase<AssociateAcceptInvite, Unit>() {
    override suspend fun implementation(input: AssociateAcceptInvite) {
        AssociateValidationUtils.validateMasterAlreadyAssociated(input.user)

        Firebase.firestore.runTransaction {
            val user = input.user.copy(
                masterSummary = input.masterSummary
            )

            // Save masterId on Chicken
            userRepository.updateUser(user, this@runTransaction)
            mutableUserSession.updateUser(user)

            // Save UserSummary on Master
            userRepository.addChicken(
                input.masterSummary.masterId,
                user.toChickenSummary(),
                this@runTransaction
            )

            // Accept invite
            associateRepository.updateInvite(
                input.associateInvite.copy(
                    accepted = true
                ),
                this@runTransaction
            )
        }
    }
}
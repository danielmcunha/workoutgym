package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.data.session.UserSession
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.associate.validation.AssociateValidationUtils
import com.southapps.domain.common.useCase.UseCase

class AssociateCheckInviteUseCase(
    private val associateRepository: AssociateRepository,
    private val userSession: UserSession,
) : UseCase<String, AssociateInvite?>() {
    override suspend fun implementation(input: String): AssociateInvite? {
        AssociateValidationUtils.canShowInvite(userSession.currentUser.value?.masterSummary)

        val invite = associateRepository.checkOpenInvite(input)
        AssociateValidationUtils.canAcceptInvite(invite)

        return invite
    }
}
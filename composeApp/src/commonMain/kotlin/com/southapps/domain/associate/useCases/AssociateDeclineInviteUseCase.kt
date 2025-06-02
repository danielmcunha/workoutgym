package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.common.useCase.UseCase

class AssociateDeclineInviteUseCase(
    private val associateRepository: AssociateRepository,
) : UseCase<AssociateInvite, Unit>() {
    override suspend fun implementation(input: AssociateInvite) {
        associateRepository.updateInvite(
            input.copy(
                accepted = false
            )
        )
    }
}
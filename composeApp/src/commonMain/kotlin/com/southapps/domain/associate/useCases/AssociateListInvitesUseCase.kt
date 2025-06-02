package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.common.useCase.UseCase

class AssociateListInvitesUseCase(
    private val associateRepository: AssociateRepository,
) : UseCase<String, List<AssociateInvite>>() {
    override suspend fun implementation(input: String): List<AssociateInvite> {
        return associateRepository.getOpenInvites(input)
    }
}
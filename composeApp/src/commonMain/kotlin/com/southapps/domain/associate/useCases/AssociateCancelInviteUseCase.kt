package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.domain.common.useCase.UseCase

class AssociateCancelInviteUseCase(
    private val associateRepository: AssociateRepository,
) : UseCase<String, Unit>() {
    override suspend fun implementation(input: String) {
        associateRepository.deleteInvite(input)
    }
}
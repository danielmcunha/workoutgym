package com.southapps.domain.associate.useCases

import com.southapps.data.associate.AssociateRepository
import com.southapps.data.session.UserSession
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.associate.validation.AssociateValidationUtils
import com.southapps.domain.common.useCase.FormUseCase
import com.southapps.domain.common.validation.ValidationUtils
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.master.entities.toMasterSummary

class AssociateSendInviteUseCase(
    private val associateRepository: AssociateRepository,
    private val userSession: UserSession,
) : FormUseCase<FormField, Unit>() {

    override suspend fun implementation(input: FormField) {
        val user = ValidationUtils.validateUserSession(userSession)

        validate(
            input.copy(
            customValidator =
                {
                    AssociateValidationUtils.validateSameEmail(
                        user.email.orEmpty(),
                        it.value.toString()
                    )
                }

        ))

        val invites = associateRepository.getInvites(input.value.toString())
        AssociateValidationUtils.validateInviteAlreadyExists(invites)

        val masterSummary = user.toMasterSummary()
        associateRepository.createInvite(
            associateInvite = AssociateInvite(
                masterSummary = masterSummary,
                chickenEmail = input.value.toString()
            )
        )
    }
}

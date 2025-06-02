package com.southapps.domain.associate.validation

import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.common.exception.BusinessException
import com.southapps.domain.common.validation.ValidationError
import com.southapps.domain.common.validation.ValidationResult
import com.southapps.domain.master.entities.MasterSummary
import com.southapps.domain.user.entities.User

object AssociateValidationUtils {

    fun validateSameEmail(currentEmail: String, email: String): ValidationResult? {
        if (currentEmail == email) {
            return AssociateValidationError.SameEmail
        }

        return null
    }

    fun validateMasterAlreadyAssociated(user: User) {
        if (user.masterSummary != null) {
            BusinessException.emit(AssociateValidationError.MasterAlreadyAssociated)
        }
    }

    fun validateInviteAlreadyExists(invites: List<AssociateInvite>) {
        if (invites.any { it.accepted == null }) {
            BusinessException.emit(AssociateValidationError.InviteAlreadyExists)
        }
    }

    fun canShowInvite(masterSummary: MasterSummary?) {
        if (masterSummary != null) {
            BusinessException.emit(AssociateValidationError.AssociationAlreadyExists)
        }
    }

    fun canAcceptInvite(associateInvite: AssociateInvite?) {
        if (associateInvite?.accepted != null) {
            BusinessException.emit(AssociateValidationError.InviteAlreadyAccepted)
        }
    }

}
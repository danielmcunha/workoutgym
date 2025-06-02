package com.southapps.domain.associate.validation

import com.southapps.domain.common.validation.ValidationResult

sealed class AssociateValidationError(message: String) : ValidationResult(message) {
    data object MasterAlreadyAssociated : AssociateValidationError("Você já possuí um treinador associado, para aceitar este convite é necessário remover o seu treinador atual")
    data object SameEmail : AssociateValidationError("Você não pode enviar um convite para você mesmo")
    data object InvalidEmail : AssociateValidationError("E-mail inválido")
    data object BlankEmail : AssociateValidationError("Email não pode ser vazio")
    data object InviteAlreadyExists : AssociateValidationError("Convite já enviado para esse usuário")
    data object AssociationAlreadyExists : AssociateValidationError("Você já tem um treinador associado")
    data object InviteAlreadyAccepted : AssociateValidationError("Convite já aceito")
}
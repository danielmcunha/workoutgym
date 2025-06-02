package com.southapps.domain.common.validation

sealed class ValidationError(message: String) : ValidationResult(message) {
    data class EmptyField(val field: String) : ValidationError("O campo ${field} não pode ser vazio")
    data object InvalidEmail : ValidationError("E-mail inválido")
    data object UnknownAuthorized : ValidationError("Sem permissão")
    data object PasswordLengthRule : ValidationError("A senha deve conter pelo menos 8 caracteres")
    data class ConfirmationNotMatch(val field: String) : ValidationError("O campo ${field} não confere")

    // routine
    data object InvalidTime : ValidationError("Horário inválido")
    data object MissingWorkout : ValidationError("É necessário selecionar um treino")
    data object InvalidFrequency : ValidationError("Frequência inválida")
    data object MissingDays : ValidationError("Selecione ao menos um dia da semana")
    data object MissingLocation : ValidationError("Informe o local do treino")
}
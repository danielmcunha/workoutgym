package com.southapps.domain.common.validation

import com.southapps.data.session.UserSession
import com.southapps.domain.common.exception.UnknownAuthorizedException
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.user.entities.User

object ValidationUtils {

    fun validateFormField(formField: FormField): List<FormField> {
        val errors = mutableListOf<String>()

        if (!formField.optional) {
            validateEmptyField(formField.field, formField.value?.toString())?.let { errors.add(it.error) }
        }

        errors.ifEmpty { formField.type.validator?.invoke(formField)?.let { errors.add(it.error) } }
        errors.ifEmpty { formField.customValidator?.invoke(formField)?.let { errors.add(it.error) } }

        return if (errors.isEmpty()) emptyList()
        else listOf(formField.copy(error = errors.joinToString("\n")))
    }
    fun validateEmptyField(field: String, value: String?): ValidationResult? {
        return if (value.isNullOrEmpty()) ValidationError.EmptyField(field) else null
    }

    fun validatePassword(field: String, password: String?): ValidationResult? {
        if (password.isNullOrEmpty()) return ValidationError.EmptyField(field)
        if (password.length < 8) return ValidationError.PasswordLengthRule
        return null
    }

    fun validateEmail(field: String, email: String?): ValidationError? {
        if (email.isNullOrEmpty()) return ValidationError.EmptyField(field)

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return if (!email.matches(emailRegex.toRegex())) {
            ValidationError.InvalidEmail
        } else null
    }

    fun validateConfirmField(field: String, value1: String?, value2: String?): ValidationError? {
        return if (value1 != value2) {
            ValidationError.ConfirmationNotMatch(field)
        } else null
    }

    fun validateUserSession(userSession: UserSession): User {
        return userSession.currentUser.value
            ?: throw UnknownAuthorizedException()
    }
}
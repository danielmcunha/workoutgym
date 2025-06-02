package com.southapps.domain.common.exception

import com.southapps.domain.common.validation.ValidationResult

class BusinessException(
    val validations: List<ValidationResult>
) : Exception(validations.joinToString("; ") { it.error }) {

    constructor(validation: ValidationResult) : this(listOf(validation))

    companion object {
        fun emit(validation: ValidationResult): Nothing {
            throw BusinessException(listOf(validation))
        }

        fun emit(vararg validations: ValidationResult): Nothing {
            throw BusinessException(validations.toList())
        }

        fun emit(validations: List<ValidationResult>): Nothing {
            throw BusinessException(validations)
        }
    }
}
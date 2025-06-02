package com.southapps.domain.common.validation.form

import com.southapps.domain.common.validation.ValidationUtils

object FormValidation {
    fun validate(fields: List<FormField>): List<FormField> {
        return fields.flatMap { field ->
            ValidationUtils.validateFormField(field)
        }
    }
}
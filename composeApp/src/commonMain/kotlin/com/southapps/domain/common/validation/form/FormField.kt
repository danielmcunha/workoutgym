package com.southapps.domain.common.validation.form

import com.southapps.domain.common.validation.ValidationError
import com.southapps.domain.common.validation.ValidationResult
import com.southapps.domain.common.validation.ValidationUtils


enum class FormFieldType(val validator: ((FormField) -> ValidationResult?)?) {
    StringType(null),
    Numeric(null),
    Email({ ValidationUtils.validateEmail(it.field, it.value?.toString()) }),
    Date(null),
    Password({ ValidationUtils.validatePassword(it.field, it.value?.toString()) }),
    Confirmation({
        val values = it.value?.toString()?.split("=")
        if (values?.size == 2) {
            ValidationUtils.validateConfirmField(it.field, values[0], values[1])
        } else {
            ValidationError.ConfirmationNotMatch(it.field)
        }
    })
}

data class FormField(
    val field: String,
    val value: Any? = null,
    val type: FormFieldType = FormFieldType.StringType,
    val optional: Boolean = false,
    val error: String? = null,
    val customValidator: ((FormField) -> ValidationResult?)? = null)

fun <T> T?.toFormField(
    field: String,
    type: FormFieldType = FormFieldType.StringType,
    optional: Boolean = false
) = FormField(field, value = this, type = type, optional = optional)
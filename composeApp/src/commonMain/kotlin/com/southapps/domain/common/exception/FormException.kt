package com.southapps.domain.common.exception

import com.southapps.domain.common.validation.form.FormField

class FormException(val fields: List<FormField>) : Exception() {
    companion object {
        fun emit(fields: List<FormField>) {
            throw FormException(fields)
        }
    }
}
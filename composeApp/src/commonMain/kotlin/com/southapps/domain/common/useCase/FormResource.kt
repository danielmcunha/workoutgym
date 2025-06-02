package com.southapps.domain.common.useCase

import com.southapps.domain.common.validation.form.FormField

typealias FormEmitter = suspend (List<FormField>) -> Unit

sealed interface FormResource<T> {
    data class Success<T>(val data: T) : FormResource<T>
    data class Error<T>(val exception: Throwable?) : FormResource<T>

    data class FormError<T>(val errorFormFields: List<FormField>) : FormResource<T>

    val isSuccess: Boolean
        get() = this is Success

    val success: Success<T>?
        get() = this as? Success<T>
}
package com.southapps.domain.common.useCase

import com.southapps.domain.common.exception.BusinessException
import com.southapps.domain.common.exception.FormException
import com.southapps.domain.common.exception.GenericException
import com.southapps.domain.common.exception.UnknownAuthorizedException
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.common.validation.form.FormValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class FormUseCase<I, R> {

    private var emitter: FormEmitter? = null

    internal abstract suspend fun implementation(
        input: I,
    ): R

    internal open suspend fun onCatch(error: Throwable) {}

    fun execute(input: I) =
        flow {
            emitter = { this@flow.emit(FormResource.FormError(it)) }

            emit(
                FormResource.Success(
                    implementation(input)
                )
            )

        }.catch { error ->
            onCatch(error)
            when (error) {
                is FormException -> emit(FormResource.FormError(error.fields))
                is BusinessException -> emit(FormResource.Error(error))
                is UnknownAuthorizedException -> emit(FormResource.Error(error))
                else -> emit(FormResource.Error(GenericException()))
            }
        }.flowOn(Dispatchers.IO)


    fun validate(vararg fields: FormField) {
        val errors = FormValidation.validate(fields.toList())
        if (errors.isNotEmpty()) {
            FormException.emit(errors)
        }
    }
}
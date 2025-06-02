package com.southapps.domain.common.useCase

import com.southapps.domain.common.exception.BusinessException
import com.southapps.domain.common.exception.GenericException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<I, R> {

    internal abstract suspend fun implementation(input: I): R
    internal open suspend fun onCatch(error: Throwable) {}

    fun execute(input: I) = flow<Resource<R>> { emit(Resource.Success(implementation(input))) }
        .catch {
            onCatch(it)

            when (it) {
                is BusinessException -> emit(Resource.Error(it))
                else -> emit(Resource.Error(GenericException()))
            }
        }
        .flowOn(Dispatchers.IO)
}
package com.southapps.domain.user.useCases

import com.southapps.data.user.UserRepository
import com.southapps.domain.common.useCase.UseCase

class UserSendPasswordResetEmailUseCase(
    private val repository: UserRepository
) : UseCase<String, Unit>() {
    override suspend fun implementation(input: String) {
        repository.sendPasswordResetEmail(input)
    }
}

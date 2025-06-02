package com.southapps.domain.user.useCases

import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.common.validation.ValidationUtils
import com.southapps.domain.user.entities.User

class UserEditUseCase(
    private val repository: UserRepository,
    private val mutableUserSession: MutableUserSession,
) : UseCase<User, Unit>() {
    override suspend fun implementation(input: User) {
        val user = ValidationUtils.validateUserSession(mutableUserSession)

        val editUser = user.copy(
            bornDate = input.bornDate,
            gender = input.gender,
            bodyInfo = input.bodyInfo,
            goals = input.goals
        )

        repository.updateUser(editUser)
        mutableUserSession.updateUser(editUser)
    }
}
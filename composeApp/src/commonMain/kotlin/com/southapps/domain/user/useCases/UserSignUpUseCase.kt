package com.southapps.domain.user.useCases

import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.domain.common.useCase.FormUseCase
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserRegisterForm
import com.southapps.domain.user.entities.UserType

class UserSignUpUseCase(
    private val userRepository: UserRepository,
    private val mutableUserSession: MutableUserSession,
) : FormUseCase<UserRegisterForm, User>() {

    override suspend fun implementation(
        input: UserRegisterForm
    ): User {
        validate(
            input.name,
            input.email,
            input.phone,
            input.password,
            input.confirmPassword
        )

        val user = User(
            name = input.name.value.toString(),
            email = input.email.value.toString(),
            phone = input.phone.value.toString(),
            type = UserType.Chicken
        )

        return userRepository.registerWithEmailAndPassword(
            user,
            input.password.value.toString()
        ).apply {
            mutableUserSession.updateUser(this)
        }
    }
}

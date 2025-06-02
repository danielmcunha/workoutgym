package com.southapps.domain.user.useCases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.data.user.saveUserSession
import com.southapps.domain.common.useCase.UseCase
import com.southapps.domain.common.validation.ValidationUtils
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserSignIn

class UserSignInUseCase(
    private val repository: UserRepository,
    private val mutableUserSession: MutableUserSession,
    private val dataStore: DataStore<Preferences>
) : UseCase<UserSignIn, User>() {
    override suspend fun implementation(input: UserSignIn): User {
        ValidationUtils.validateEmail("E-mail", input.email)
        ValidationUtils.validatePassword("Senha", input.password)

        return repository.signIn(input.email, input.password).apply {
            mutableUserSession.updateUser(this)
            mutableUserSession.setSessionError(null)
            dataStore.saveUserSession(this)

            try {
                dataStore.saveUserSession(this)
            } catch (_: Exception) {

            }
        }
    }
}

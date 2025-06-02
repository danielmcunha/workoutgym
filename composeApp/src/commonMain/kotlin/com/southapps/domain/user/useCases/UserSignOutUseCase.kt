package com.southapps.domain.user.useCases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.southapps.data.session.MutableUserSession
import com.southapps.data.user.UserRepository
import com.southapps.data.user.clearUserSession
import com.southapps.domain.common.useCase.UseCase

class UserSignOutUseCase(
    private val repository: UserRepository,
    private val mutableUserSession: MutableUserSession,
    private val dataStore: DataStore<Preferences>
) : UseCase<Unit, Unit>() {
    override suspend fun implementation(input: Unit) {
        repository.signOut()
        mutableUserSession.clearAllSessionData()
        dataStore.clearUserSession()
    }
}

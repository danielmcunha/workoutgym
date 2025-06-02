package com.southapps.domain.user.useCases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.southapps.data.DataNotFoundException
import com.southapps.data.NetworkException
import com.southapps.data.session.MutableUserSession
import com.southapps.data.session.UserSessionError
import com.southapps.data.user.UserRepository
import com.southapps.data.user.clearUserSession
import com.southapps.data.user.getUserSession
import com.southapps.data.user.saveUserSession
import com.southapps.domain.common.useCase.UseCase
import kotlinx.coroutines.flow.firstOrNull
import okio.IOException

class LoadUserSessionUseCase(
    private val repository: UserRepository,
    private val mutableUserSession: MutableUserSession,
    private val dataStore: DataStore<Preferences>
) : UseCase<Unit, Unit>() {
    override suspend fun implementation(input: Unit) {
        var userLoadedFromDataStore = false

        try {
            val userFromDataStore = dataStore.getUserSession().firstOrNull()
            if (userFromDataStore != null) {
                mutableUserSession.updateUser(userFromDataStore)
                userLoadedFromDataStore = true
                mutableUserSession.setSessionError(null)
            }
        } catch (e: IOException) {
            //Log.e("LoadUserSession", "Error reading from DataStore", e)
        } catch (e: Exception) {
            // Log.e("LoadUserSession", "Unexpected error reading from DataStore", e)
            mutableUserSession.setSessionError(
                UserSessionError.DataStoreError(
                    "Erro inesperado ao ler sessão local.",
                )
            )
        }


        val currentUserId = repository.getCurrentAuthenticatedUserId()
        if (currentUserId == null) {
            // invalid user
            if (userLoadedFromDataStore) {
                try {
                    dataStore.clearUserSession()
                } catch (_: Exception) {
                }
            }

            mutableUserSession.clearAllSessionData()
            mutableUserSession.setSessionError(UserSessionError.UserNotAuthenticated)
            return
        }

        repository.getChickens().apply {
            mutableUserSession.updateChickens(this)
        }

        repository.getUser(currentUserId).apply {
            mutableUserSession.updateUser(this)
            try {
                dataStore.saveUserSession(this)
            } catch (e: Exception) {
                // Log.w("LoadUserSession", "Failed to save updated user to DataStore", e)
            }
        }
    }

    override suspend fun onCatch(error: Throwable) {
        when (error) {
            is DataNotFoundException -> {
                clearSessionAndSetError(
                    UserSessionError.UserDataNotFound,
                    attemptFirebaseSignOut = true
                )
            }

            is NetworkException -> {
                mutableUserSession.setSessionError(UserSessionError.NetworkError(error.message))
            }

            is IOException -> {
                clearSessionAndSetError(
                    UserSessionError.DataStoreError(
                        "Falha de I/O na sessão: ${error.message}",
                    )
                )
            }

            else -> {
                clearSessionAndSetError(
                    UserSessionError.UnknownError(
                        "Falha ao carregar sessão: ${error.message}",
                    )
                )
            }
        }
    }

    private suspend fun clearSessionAndSetError(
        sessionError: UserSessionError,
        attemptFirebaseSignOut: Boolean = false
    ) {
        try {
            dataStore.clearUserSession()
        } catch (_: Exception) {
        }
        if (attemptFirebaseSignOut) {
            try {
                repository.signOut()
            } catch (_: Exception) {
            }
        }
        mutableUserSession.clearAllSessionData()
        mutableUserSession.setSessionError(sessionError)
    }
}
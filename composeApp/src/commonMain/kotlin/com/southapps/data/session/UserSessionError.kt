package com.southapps.data.session

sealed class UserSessionError {
    data object UserNotAuthenticated : UserSessionError()
    data object UserDataNotFound : UserSessionError()
    data class NetworkError(val originalMessage: String?) : UserSessionError()
    data class AuthenticationError(val message: String?) : UserSessionError()
    data class DataStoreError(val message: String?) : UserSessionError()
    data class UnknownError(val message: String?) : UserSessionError()
}
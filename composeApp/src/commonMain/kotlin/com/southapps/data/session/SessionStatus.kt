package com.southapps.data.session

import com.southapps.domain.user.entities.User

sealed interface SessionStatus {
    data object Initializing : SessionStatus
    data class Authenticated(val user: User) : SessionStatus
    data object Unauthenticated : SessionStatus
    data class Error(val errorDetails: UserSessionError) : SessionStatus
}
package com.southapps

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.SessionStatus
import com.southapps.data.session.UserSession
import com.southapps.data.session.UserSessionError
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.useCases.LoadUserSessionUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppEntryUiState(
    val sessionStatus: SessionStatus? = SessionStatus.Initializing
) : UIState

class AppEntryViewModel(
    navigator: Navigator,
    private val loadSessionUseCase: LoadUserSessionUseCase,
    private val userSession: UserSession
) : BaseViewModel<AppEntryUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(AppEntryUiState())
    override val uiState = _uiState.asStateFlow()

    init {
        observeUserSession()
        loadUserSession()
    }

    private fun loadUserSession() {
        viewModelScope.launch {
            try {
                loadSessionUseCase.execute(Unit).collect {}
            } catch (_: Exception) {
            }
        }
    }

    private fun getSessionStatus(user: User?, error: UserSessionError?): SessionStatus {
        return when {
            user != null -> SessionStatus.Authenticated(user)
            error != null -> when (error) {
                is UserSessionError.UserNotAuthenticated,
                is UserSessionError.UserDataNotFound -> SessionStatus.Unauthenticated

                else -> SessionStatus.Error(error)
            }

            else -> SessionStatus.Initializing
        }
    }

    private fun observeUserSession() {
        viewModelScope.launch {
            userSession.currentUser
                .combine(userSession.sessionError) { user, error ->
                    getSessionStatus(user, error)
                }
                .distinctUntilChanged()
                .collect { newStatus ->
                    if (newStatus is SessionStatus.Unauthenticated) {
                        navigator.startDestination = BaseRoute.Login
                    } else if (newStatus is SessionStatus.Authenticated) {
                        navigator.startDestination = BottomNavigationRoute.Home
                    }

                    _uiState.update {
                        it.copy(sessionStatus = newStatus)
                    }
                }
        }
    }

    fun retry() {
        if (_uiState.value.sessionStatus is SessionStatus.Error ||
            _uiState.value.sessionStatus is SessionStatus.Unauthenticated
        ) {
            loadUserSession()
        }
    }
}
package com.southapps.ui.screen.login

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.user.entities.UserSignIn
import com.southapps.domain.user.useCases.UserSignInUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUIState(
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String? = null
) : UIState

class LoginViewModel(
    navigator: Navigator,
    private val userSignInUseCase: UserSignInUseCase,
) : BaseViewModel<LoginUIState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(LoginUIState())
    override val uiState: StateFlow<LoginUIState> = _uiState

    fun signIn() {
        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            userSignInUseCase.execute(
                UserSignIn(
                    email = uiState.value.email,
                    password = uiState.value.password
                )
            ).collect { result ->
                when (result) {
                    is Resource.Error<*> -> _uiState.update {
                        it.copy(loading = false, error = result.exception?.message ?: "")
                    }

                    is Resource.Success<*> -> navigator.navigate(BottomNavigationRoute.Home)
                }
            }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun resetPassword() {
        if (uiState.value.loading) return
    }

    fun signUp() {
        if (uiState.value.loading) return

        navigator.navigate(BaseRoute.Register)
    }
}
package com.southapps.ui.screen.profile

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.UserSession
import com.southapps.domain.master.entities.MasterSummary
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserType
import com.southapps.domain.user.useCases.UserSignOutUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileViewState(
    val user: User? = null,
    val masterSummary: MasterSummary? = null,
    val showBeATrainer: Boolean = false,
    val showFindATrainer: Boolean = false,
) : UIState

class ProfileViewModel(
    navigator: Navigator,
    private val userSession: UserSession,
    private val userSignOutUseCase: UserSignOutUseCase,
) :
    BaseViewModel<ProfileViewState, UIAction>(navigator) {
    private val _uiState = MutableStateFlow(ProfileViewState())
    override val uiState: StateFlow<ProfileViewState> = _uiState
        .onStart { loadProfile() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProfileViewState()
        )

    private fun loadProfile() {
        viewModelScope.launch {
            userSession.currentUser.collect { user ->
                if (user == null) {
                    sendAction(UIAction.ShowSnackbar("Não foi possível carregar o perfil"))
                } else {
                    val showBeATrainer = user.type != UserType.Master
                    val showFindATrainer =
                        user.masterSummary == null && user.type != UserType.Master

                    _uiState.update {
                        it.copy(
                            user = user,
                            masterSummary = user.masterSummary,
                            showBeATrainer = showBeATrainer,
                            showFindATrainer = showFindATrainer
                        )
                    }
                }
            }
        }
    }

    fun onManageMasterClick() {
        navigator.navigate(_uiState.value.masterSummary?.let {
            BaseRoute.UserManageMaster(
                masterSummary = it,
                transitionKey = it.masterId
            )
        })
    }

    fun logOut() {
        sendAction(
            UIAction.ShowDialog(
                "Sair do app",
                "Você realmente deseja sair do app?",
                cancelButton = Pair("Não") { },
                confirmButton = Pair("Sim") {
                    viewModelScope.launch {
                        userSignOutUseCase.execute(Unit).collect { }
                    }
                },
            )
        )
    }
}
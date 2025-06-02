package com.southapps.ui.screen.associate.master.listInvites

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.UserSession
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.associate.useCases.AssociateCancelInviteUseCase
import com.southapps.domain.associate.useCases.AssociateListInvitesUseCase
import com.southapps.domain.common.useCase.Resource
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AssociateListInvitesViewState(
    val invites: List<AssociateInvite>? = null,
    val isLoading: Boolean = false,
) : UIState

class AssociateListInvitesViewModel(
    navigator: Navigator,
    private val userSession: UserSession,
    private val associateListInvitesUseCase: AssociateListInvitesUseCase,
    private val associateCancelInviteUseCase: AssociateCancelInviteUseCase,
) : BaseViewModel<AssociateListInvitesViewState, UIAction>(navigator) {

    private var _uiState = MutableStateFlow(AssociateListInvitesViewState(isLoading = true))
    override val uiState: StateFlow<AssociateListInvitesViewState> = _uiState
        .onStart { fetchInvites() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AssociateListInvitesViewState(isLoading = true)
        )

    fun onCancelClick(uid: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            associateCancelInviteUseCase
                .execute(uid)
                .collect { result ->
                    when (result) {
                        is Resource.Error<*> -> sendAction(UIAction.ShowSnackbar("Ops, não foi possível cancelar o envio do convite"))
                        is Resource.Success<*> -> fetchInvites()
                    }
                }
        }
    }

    private suspend fun fetchInvites() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        associateListInvitesUseCase
            .execute(userSession.currentUser.value?.uid.orEmpty())
            .collect { result ->
                when (result) {
                    is Resource.Error<*> -> sendAction(UIAction.ShowSnackbar(result.exception?.message.orEmpty()))
                    is Resource.Success<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                invites = result.success?.data
                            )
                        }
                    }
                }
            }
    }

    fun sendNewInvite() {
        navigator.navigate(BaseRoute.AssociateSendInviteScreen, popBack = true)
    }
}
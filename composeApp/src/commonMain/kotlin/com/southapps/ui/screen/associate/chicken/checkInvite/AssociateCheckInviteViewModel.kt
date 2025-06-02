package com.southapps.ui.screen.associate.chicken.checkInvite

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.UserSession
import com.southapps.domain.associate.entities.AssociateAcceptInvite
import com.southapps.domain.associate.entities.AssociateInvite
import com.southapps.domain.associate.useCases.AssociateAcceptInviteUseCase
import com.southapps.domain.associate.useCases.AssociateCheckInviteUseCase
import com.southapps.domain.associate.useCases.AssociateDeclineInviteUseCase
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.master.entities.MasterSummary
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AssociateCheckInviteViewState(
    val isVisible: Boolean? = false,
    val masterSummary: MasterSummary? = null,
    val associateInvite: AssociateInvite? = null,
    val isAcceptLoading: Boolean? = false,
    val isDeclineLoading: Boolean? = false,
) : UIState

class AssociateCheckInviteViewModel(
    navigator: Navigator,
    private val associateCheckInviteUseCase: AssociateCheckInviteUseCase,
    private val associateAcceptInviteUseCase: AssociateAcceptInviteUseCase,
    private val associateDeclineInviteUseCase: AssociateDeclineInviteUseCase,
    private val userSession: UserSession,
) :
    BaseViewModel<AssociateCheckInviteViewState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(AssociateCheckInviteViewState())
    override val uiState: StateFlow<AssociateCheckInviteViewState> =
        _uiState
            .onStart { checkInvite() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(500L),
                AssociateCheckInviteViewState()
            )

    fun acceptInvite() {
        _uiState.update {
            it.copy(
                isAcceptLoading = true,
                isDeclineLoading = false
            )
        }

        viewModelScope.launch {
            userSession.currentUser.value?.let { user ->
                _uiState.value.masterSummary?.let { masterSummary ->
                    _uiState.value.associateInvite?.let { associateInvite ->
                        associateAcceptInviteUseCase
                            .execute(
                                AssociateAcceptInvite(
                                    user,
                                    masterSummary,
                                    associateInvite
                                )
                            )
                            .collect { result ->
                                when (result) {
                                    is Resource.Error<*> -> {
                                        _uiState.update {
                                            it.copy(
                                                isAcceptLoading = false,
                                                isDeclineLoading = false,
                                            )
                                        }

                                        sendAction(UIAction.ShowSnackbar("Ops, tivemos um erro ao aceitar o convite"))
                                    }

                                    is Resource.Success<*> -> {
                                        _uiState.update {
                                            it.copy(
                                                isAcceptLoading = false,
                                                isDeclineLoading = false,
                                                isVisible = false
                                            )
                                        }

                                        sendAction(UIAction.ShowSnackbar("Convite aceito!"))
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    fun declineInvite() {
        _uiState.update {
            it.copy(
                isAcceptLoading = true,
                isDeclineLoading = false
            )
        }

        viewModelScope.launch {
            _uiState.value.associateInvite?.let { associateInvite ->
                associateDeclineInviteUseCase
                    .execute(associateInvite)
                    .collect { result ->
                        when (result) {
                            is Resource.Error<*> -> {
                                _uiState.update {
                                    it.copy(
                                        isAcceptLoading = false,
                                        isDeclineLoading = false,
                                    )
                                }

                                sendAction(UIAction.ShowSnackbar("Ops, tivemos um erro ao rejeitar o convite"))
                            }

                            is Resource.Success<*> -> {
                                _uiState.update {
                                    it.copy(
                                        isAcceptLoading = false,
                                        isDeclineLoading = false,
                                        isVisible = false
                                    )
                                }

                                sendAction(UIAction.ShowSnackbar("Convite rejeitado!"))
                            }
                        }
                    }
            }
        }
    }

    private suspend fun checkInvite() {
        userSession.currentUser.value?.email?.let { email ->
            associateCheckInviteUseCase
                .execute(email)
                .collect { result ->
                    result.success?.let { resultSuccess ->
                        if(resultSuccess.data == null) return@collect

                        _uiState.update {
                            it.copy(
                                isVisible = true,
                                masterSummary = resultSuccess.data.masterSummary,
                                associateInvite = resultSuccess.data
                            )
                        }
                    }
                }
        }
    }
}
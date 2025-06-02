package com.southapps.ui.screen.associate.master.sendInvite

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.associate.useCases.AssociateSendInviteUseCase
import com.southapps.domain.common.useCase.FormResource
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.common.validation.form.FormFieldType
import com.southapps.domain.common.validation.form.toFormField
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val EMAIL = "E-mail"

data class AssociateSendInviteViewState(
    val isLoading: Boolean = false,
    val email: FormField = "".toFormField(EMAIL, type = FormFieldType.Email),
) : UIState

class AssociateSendInviteViewModel(
    navigator: Navigator,
    private val associateSendInviteUseCase: AssociateSendInviteUseCase,
) :
    BaseViewModel<AssociateSendInviteViewState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(AssociateSendInviteViewState())
    override val uiState: StateFlow<AssociateSendInviteViewState> = _uiState

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = it.email.copy(value = email))
        }
    }

    fun onAssociateClick() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            associateSendInviteUseCase
                .execute(_uiState.value.email)
                .collect { result ->
                    _uiState.update {
                        it.copy(isLoading = false)
                    }

                    when (result) {
                        is FormResource.Error<*> -> sendAction(UIAction.ShowSnackbar(result.exception?.message.orEmpty()))
                        is FormResource.FormError<*> -> _uiState.update {
                            it.copy(email = result.errorFormFields[0])
                        }

                        is FormResource.Success<*> -> {
                            sendAction(UIAction.ShowSnackbar("Convite enviado!"))

                            navigator.navigate(BaseRoute.AssociateListInvitesScreen, popBack = true)
                        }
                    }
                }
        }
    }
}
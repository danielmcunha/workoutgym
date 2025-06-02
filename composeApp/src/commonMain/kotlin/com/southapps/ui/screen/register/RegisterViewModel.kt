package com.southapps.ui.screen.register

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.common.useCase.FormResource
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.common.validation.form.FormFieldType
import com.southapps.domain.common.validation.form.toFormField
import com.southapps.domain.user.entities.UserRegisterForm
import com.southapps.domain.user.useCases.UserSignUpUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import com.southapps.ui.navigation.routes.OnboardingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val NAME = "Nome"
private const val PHONE = "Telefone"
private const val EMAIL = "E-mail"
private const val PASSWORD = "Senha"
private const val PASSWORD_CONFIRMATION = "Confirmação de senha"

data class RegisterUIState(
    val loading: Boolean = false,
    val fullName: FormField = "".toFormField(NAME),
    val phone: FormField = "".toFormField(PHONE),
    val email: FormField = "".toFormField(EMAIL, type = FormFieldType.Email),
    val password: FormField = "".toFormField(PASSWORD, type = FormFieldType.Password),
    val passwordConfirmation: FormField = "".toFormField(
        PASSWORD_CONFIRMATION,
        type = FormFieldType.Confirmation
    ),
    val error: String? = null,
) : UIState

class RegisterViewModel(
    navigator: Navigator,
    private val userSignUpUseCase: UserSignUpUseCase,
) : BaseViewModel<RegisterUIState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(RegisterUIState())
    override val uiState: StateFlow<RegisterUIState> = _uiState

    private fun setLoading(boolean: Boolean) {
        _uiState.update {
            it.copy(loading = boolean)
        }
    }

    private fun setError(message: String) {
        _uiState.update {
            it.copy(
                loading = false,
                error = message
            )
        }
    }

    fun updateFullName(fullName: String) {
        _uiState.update {
            it.copy(fullName = it.fullName.copy(value = fullName))
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = it.email.copy(value = email))
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(phone = it.phone.copy(value = phone))
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = it.password.copy(value = password))
        }
    }

    fun updatePasswordConfirmation(passwordConfirmation: String) {
        _uiState.update {
            it.copy(
                passwordConfirmation =
                    it.passwordConfirmation.copy(
                        value = passwordConfirmation
                    )
            )
        }
    }

    fun signIn() {
        navigator.navigate(BaseRoute.Login)
    }

    fun signUp() {
        setLoading(true)

        val data = _uiState.value

        viewModelScope.launch {
            userSignUpUseCase.execute(
                UserRegisterForm(
                    email = data.email,
                    name = data.fullName,
                    phone = data.phone,
                    password = data.password,
                    confirmPassword = data.passwordConfirmation.copy(
                        value = "${data.password.value}=${data.passwordConfirmation.value}"
                    )
                )
            ).collect { result ->
                when (result) {
                    is FormResource.Error<*> -> {
                        setError(result.exception?.message.orEmpty())
                    }

                    is FormResource.Success<*> -> navigator.navigate(OnboardingRoute.Onboarding)
                    is FormResource.FormError<*> -> {
                        result.errorFormFields.forEach { formField ->
                            _uiState.value = when (formField.field) {
                                NAME -> _uiState.value.copy(fullName = formField)
                                EMAIL -> _uiState.value.copy(email = formField)
                                PHONE -> _uiState.value.copy(phone = formField)
                                PASSWORD -> _uiState.value.copy(password = formField)
                                PASSWORD_CONFIRMATION -> _uiState.value.copy(
                                    passwordConfirmation = formField.copy(
                                        value = _uiState.value.passwordConfirmation.value
                                    )
                                )

                                else -> {
                                    _uiState.value
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

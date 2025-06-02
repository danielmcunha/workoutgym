package com.southapps.ui.screen.onboarding.welcome

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.OnboardingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingWelcomeViewState : UIState

class OnboardingWelcomeViewModel(navigator: Navigator) :
    BaseViewModel<OnboardingWelcomeViewState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(OnboardingWelcomeViewState())
    override val uiState: StateFlow<OnboardingWelcomeViewState> = _uiState

    fun start() {
        navigator.navigate(OnboardingRoute.SelectYourJourney)
    }
}
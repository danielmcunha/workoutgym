package com.southapps.ui.screen.associate.master.remove

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AssociateRemoveChickenViewState(
    val isLoading: Boolean = false,
) : UIState

class AssociateRemoveChickenViewModel(navigator: Navigator) :
    BaseViewModel<AssociateRemoveChickenViewState, UIAction>(navigator) {

    private val _uiAction = MutableStateFlow(AssociateRemoveChickenViewState())
    override val uiState: StateFlow<AssociateRemoveChickenViewState> = _uiAction
}
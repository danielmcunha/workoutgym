package com.southapps.ui.screen.associate.chicken.remove

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AssociateRemoveMasterViewState(
    val isLoading: Boolean = false,
) : UIState

class AssociateRemoveMasterViewModel(navigator: Navigator) :
    BaseViewModel<AssociateRemoveMasterViewState, UIAction>(navigator) {

    private val _uiAction = MutableStateFlow(AssociateRemoveMasterViewState())
    override val uiState: StateFlow<AssociateRemoveMasterViewState> = _uiAction
}
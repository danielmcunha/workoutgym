package com.southapps.ui.screen.profile.manageMaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.associate.useCases.AssociateChickenRemoveMasterUseCase
import com.southapps.domain.master.entities.MasterSummary
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BaseRoute
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ManageMasterViewState(
    val masterSummary: MasterSummary,
    val sharedTransitionKey: String = "",
) : UIState

class ManageMasterViewModel(
    navigator: Navigator,
    private val associateChickenRemoveMasterUseCase: AssociateChickenRemoveMasterUseCase,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<ManageMasterViewState, UIAction>(navigator) {

    private val arguments = savedStateHandle.toRoute<BaseRoute.UserManageMaster>()

    private val _uiState = MutableStateFlow(
        ManageMasterViewState(
            arguments.masterSummary,
            arguments.transitionKey
        )
    )
    override val uiState: StateFlow<ManageMasterViewState> = _uiState

    fun removeTrainer() {
        sendAction(
            UIAction.ShowDialog(
                title = "Remover treinador",
                message = "Você tem certeza que deseja remover o seu treinador? Essa ação não poderá ser revertida, para uma nova associação será necessário receber outro convite",
                cancelButton = Pair("Cancelar") { },
                confirmButton = Pair("Remover") {
                    viewModelScope.launch {
                        associateChickenRemoveMasterUseCase
                            .execute(Unit)
                            .collect { result ->
                                if (result.isSuccess) {
                                    sendAction(UIAction.ShowSnackbar("Treinador removido com sucesso"))
                                    navigator.navigate(
                                        BottomNavigationRoute.Profile,
                                        popBack = true
                                    )
                                } else {
                                    sendAction(UIAction.ShowSnackbar("Ops, houve um erro ao remover o seu treinador"))
                                }
                            }
                    }
                }
            ))
    }
}
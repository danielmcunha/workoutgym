package com.southapps.ui.screen.master.chicken.list

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.master.useCases.GetChickensUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.MasterRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UiState for the chicken list screen
 */
data class ChickenListUiState(
    val isLoading: Boolean = false,
    val chickens: List<ChickenSummary> = emptyList(),
) : UIState

class ChickenListViewModel(
    navigator: Navigator,
    private val getChickensUseCase: GetChickensUseCase
) :
    BaseViewModel<ChickenListUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(ChickenListUiState())
    override val uiState = _uiState
        .onStart { fetchChickens() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ChickenListUiState()
        )

    private fun fetchChickens() {
        viewModelScope.launch {
            getChickensUseCase.execute(Unit).collect { result ->
                when (result) {
                    is Resource.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }

                    is Resource.Success<List<ChickenSummary>> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                chickens = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun onDetail(userId: String, userPath: String) {
        navigator.navigate(
            MasterRoute.MasterChickenDetail(
                chickenId = userId,
                chickenPath = userPath
            )
        )
    }
}
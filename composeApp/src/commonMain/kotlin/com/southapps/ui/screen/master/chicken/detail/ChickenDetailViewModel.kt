package com.southapps.ui.screen.master.chicken.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.core.extensions.toJson
import com.southapps.data.session.UserSession
import com.southapps.domain.associate.useCases.AssociateMasterRemoveChickenUseCase
import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import com.southapps.ui.navigation.routes.MasterRoute
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * UiState for the chicken list screen
 */
data class ChickenDetailUiState(
    val isLoading: Boolean = false,
    val key: String = "",
    val summary: ChickenSummary,
) : UIState

class ChickenDetailViewModel(
    navigator: Navigator,
    savedStateHandle: SavedStateHandle,
    private val userSession: UserSession,
    private val associateMasterRemoveChickenUseCase: AssociateMasterRemoveChickenUseCase,
) : BaseViewModel<ChickenDetailUiState, UIAction>(navigator) {

    private val arguments = savedStateHandle.toRoute<MasterRoute.MasterChickenDetail>()

    private val _uiState = MutableStateFlow(
        ChickenDetailUiState(
            key = arguments.chickenPath,
            summary = getChickenSummary()
        )
    )

    override val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ChickenDetailUiState(
                key = arguments.chickenPath,
                summary = getChickenSummary()
            )
        )

    private fun getChickenSummary() =
        userSession.chickens.value.first { it.userReference == arguments.chickenPath }

    fun onEditWorkout(workoutPath: String) {
        navigator.navigate(
            WorkoutRoute.WorkoutCreate(
                chickenId = arguments.chickenId,
                chickenPath = arguments.chickenPath,
                workoutPath = workoutPath
            )
        )
    }

    fun onAddWorkout() {
        navigator.navigate(
            WorkoutRoute.WorkoutCreate(
                chickenId = arguments.chickenId,
                chickenPath = arguments.chickenPath
            )
        )
    }

    fun removeChicken() {
        val userId = _uiState.value.summary.userId

        sendAction(
            UIAction.ShowDialog(
                title = "Remover aluno",
                message = "Você tem certeza que deseja remover o seu aluno? Essa ação não poderá ser revertida, para uma nova associação será necessário enviar outro convite e todo o histórico de treinos será perdido.",
                cancelButton = Pair("Cancelar") { },
                confirmButton = Pair("Remover") {
                    viewModelScope.launch {
                        associateMasterRemoveChickenUseCase
                            .execute(userId)
                            .collect { result ->
                                if (result.isSuccess) {
                                    sendAction(UIAction.ShowSnackbar("Treinador removido com sucesso"))
                                    navigator.navigate(
                                        BottomNavigationRoute.Home,
                                        clearStack = true
                                    )
                                } else {
                                    sendAction(UIAction.ShowSnackbar("Ops, houve um erro ao remover o seu treinador"))
                                }
                            }
                    }
                }
            ))
    }

    fun onScheduleWorkout() {
        navigator.navigate(
            WorkoutRoute.RoutineManager(
                userName = _uiState.value.summary.name,
                chickenPath = arguments.chickenPath,
                workoutsJson = _uiState.value.summary.workouts.toJson()
            )
        )
    }
}


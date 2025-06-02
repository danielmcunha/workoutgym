package com.southapps.ui.screen.workout.current

import androidx.lifecycle.viewModelScope
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.workout.entities.WorkoutProgress
import com.southapps.domain.workout.useCases.WorkoutDeleteCurrentUseCase
import com.southapps.domain.workout.useCases.WorkoutGetCurrentUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutCurrentViewState(
    val workoutProgress: WorkoutProgress? = null,
) : UIState

class WorkoutCurrentComponentViewModel(
    navigator: Navigator,
    private val workoutGetCurrentUseCase: WorkoutGetCurrentUseCase,
    private val workoutDeleteCurrentUseCase: WorkoutDeleteCurrentUseCase,
) : BaseViewModel<WorkoutCurrentViewState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(WorkoutCurrentViewState(null))
    override val uiState: StateFlow<WorkoutCurrentViewState> = _uiState

    fun fetch() {
        viewModelScope.launch {
            workoutGetCurrentUseCase.execute(Unit).collect { result ->
                when (result) {
                    is Resource.Success<*> -> _uiState.update {
                        it.copy(workoutProgress = result.data as? WorkoutProgress)
                    }

                    is Resource.Error<*> -> TODO()
                }
            }
        }
    }

    fun onCancelCurrentWorkout() {
        sendAction(
            UIAction.ShowDialog(
                "Encerrar treino",
                "Você realmente deseja encerrar o treino em andamento?",
                Pair("Não") { },
                Pair("Sim") { cancelWorkout() })
        )
    }

    fun onContinueCurrentWorkout() {
        _uiState.value.workoutProgress?.let { navigator.navigate(WorkoutRoute.Active(it)) }
    }

    private fun cancelWorkout() {
        _uiState.value.workoutProgress?.id?.let { id ->
            viewModelScope.launch {
                workoutDeleteCurrentUseCase.execute(id).collect()

                _uiState.update {
                    it.copy(
                        workoutProgress = null
                    )
                }
            }
        }
    }
}
package com.southapps.ui.screen.workout.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.workout.entities.WorkoutReport
import com.southapps.domain.workout.entities.fakeWorkout
import com.southapps.domain.workout.useCases.WorkoutReportUseCase
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

data class WorkoutReportViewState(
    val workoutReport: WorkoutReport? = null,
    val isLoading: Boolean = false,
) : UIState

class WorkoutReportViewModel(
    navigator: Navigator,
    private val workoutReportUseCase: WorkoutReportUseCase,
) :
    BaseViewModel<WorkoutReportViewState, UIAction>(navigator) {

    private var _uiState = MutableStateFlow(WorkoutReportViewState())
    override val uiState: StateFlow<WorkoutReportViewState> = _uiState.onStart {
        fetchWorkoutHistory(fakeWorkout().name)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WorkoutReportViewState()
    )

    private fun fetchWorkoutHistory(workoutName: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            workoutReportUseCase.execute(workoutName).collect { result ->
                when (result) {
                    is Resource.Error<*> -> TODO()
                    is Resource.Success<*> -> _uiState.update {
                        it.copy(
                            workoutReport = result.data as WorkoutReport,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onDeleteItemClick(id: String) {
        // show dialog

    }
}
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.core.extensions.toJson
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.routine.entities.WorkoutRoutine
import com.southapps.domain.routine.useCases.RoutineGetUseCase
import com.southapps.domain.routine.useCases.RoutineRemoveInput
import com.southapps.domain.routine.useCases.RoutineRemoveUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.WorkoutRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutRoutineManagerUiState(
    val isLoading: Boolean = false,
    val routine: List<WorkoutRoutine> = emptyList(),
    val error: String? = null
) : UIState

class WorkoutRoutineManagerViewModel(
    navigator: Navigator,
    savedStateHandle: SavedStateHandle,
    private val routineGetUseCase: RoutineGetUseCase,
    private val routineRemoveUseCase: RoutineRemoveUseCase
) : BaseViewModel<WorkoutRoutineManagerUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(WorkoutRoutineManagerUiState(isLoading = true))
    override val uiState = _uiState
        .onStart {
            fetchRoutines()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            WorkoutRoutineManagerUiState(isLoading = true)
        )

    private val arguments = savedStateHandle.toRoute<WorkoutRoute.RoutineManager>()

    init {
        viewModelScope.launch {
            navigator.navResults
                .filter { it.first == "routine_updated" && it.second == true }
                .collect {
                    fetchRoutines()
                }
        }
    }

    private fun fetchRoutines() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            routineGetUseCase.execute(arguments.chickenPath ?: "").collect { result ->
                when (result) {
                    is Resource.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception?.message.orEmpty()
                            )
                        }
                    }

                    is Resource.Success<List<WorkoutRoutine>> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                routine = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun addRoutine() {
        navigator.navigate(
            WorkoutRoute.RoutineCreate(
                chickenPath = arguments.chickenPath,
                workoutsJson = arguments.workoutsJson
            )
        )
    }

    fun onEditRoutine(routine: WorkoutRoutine) {
        navigator.navigate(
            WorkoutRoute.RoutineCreate(
                chickenPath = arguments.chickenPath,
                workoutsJson = arguments.workoutsJson,
                routineJson = routine.toJson()
            )
        )
    }

    fun removeRoutine(index: Int, routineId: String) {
        viewModelScope.launch {
            routineRemoveUseCase.execute(RoutineRemoveInput(chickenPath = arguments.chickenPath, routineId = routineId))
                .collect { result ->
                    when (result) {
                        is Resource.Error<*> -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception?.message.orEmpty()
                                )
                            }
                        }

                        is Resource.Success<Unit> -> {
                            _uiState.update {
                                it.copy(routine = it.routine.toMutableList().apply {
                                    removeAt(index)
                                })
                            }
                        }
                    }
                }
        }
    }
}
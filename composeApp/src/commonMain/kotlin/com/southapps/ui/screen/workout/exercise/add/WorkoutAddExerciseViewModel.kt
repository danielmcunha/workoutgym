package com.southapps.ui.screen.workout.exercise.add

import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.session.UserSession
import com.southapps.domain.workout.entities.MuscleGroup
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class WorkoutExerciseState(
    val exercise: WorkoutExercise,
    val isAdded: Boolean
)

data class WorkoutAddExerciseUiState(
    val isLoading: Boolean = false,
    val visibleExercises: List<WorkoutExerciseState> = emptyList(),
    val exerciseQuery: String = "",
    val muscleGroupFilter: List<MuscleGroupFilterState> = buildList {
        addAll(MuscleGroup.entries.map {
            MuscleGroupFilterState(
                filter = it,
                isSelected = false
            )
        })
    }
) : UIState

data class MuscleGroupFilterState(
    val filter: MuscleGroup,
    val isSelected: Boolean
)

class WorkoutAddExerciseViewModel(
    navigator: Navigator,
    private val userSession: UserSession
) : BaseViewModel<WorkoutAddExerciseUiState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(WorkoutAddExerciseUiState())
    override val uiState = _uiState

    private var allExercisesState: MutableList<WorkoutExerciseState> = mutableListOf()
    val selectedExercises: List<WorkoutExercise>
        get() = allExercisesState.filter { it.isAdded }.map { it.exercise }

    init {
        fetchExerciseList()
    }

    private fun fetchExerciseList() {
        val exerciseList = userSession.getAllWorkoutExercises()
        val allExercisesState = exerciseList.map { WorkoutExerciseState(it, false) }

        this.allExercisesState.addAll(allExercisesState)
        _uiState.update {
            it.copy(
                visibleExercises = allExercisesState
            )
        }
    }

    private fun applyFilters(
        query: String,
        selectedGroups: List<MuscleGroup>
    ): List<WorkoutExerciseState> {
        return allExercisesState.filter { item ->
            val matchesQuery = item.exercise.name.contains(query, ignoreCase = true)
            val matchesGroup =
                selectedGroups.isEmpty() || selectedGroups.contains(item.exercise.muscleGroup)
            matchesQuery && matchesGroup
        }
    }

    fun updateExerciseQuery(query: String) {
        _uiState.update { currentState ->
            val selectedGroups = currentState.muscleGroupFilter
                .filter { it.isSelected }
                .map { it.filter }

            currentState.copy(
                exerciseQuery = query,
                visibleExercises = applyFilters(query = query, selectedGroups = selectedGroups)
            )
        }
    }

    fun updateMuscleGroupFilter(index: Int) {
        _uiState.update { currentState ->
            val muscleGroupFilterList = currentState.muscleGroupFilter.toMutableList()
            val muscleGroupFilter = muscleGroupFilterList[index]

            muscleGroupFilterList[index] = muscleGroupFilter.copy(
                isSelected = !muscleGroupFilter.isSelected
            )

            val selectedGroups = muscleGroupFilterList
                .filter { it.isSelected }
                .map { it.filter }

            currentState.copy(
                muscleGroupFilter = muscleGroupFilterList,
                visibleExercises = applyFilters(
                    query = currentState.exerciseQuery,
                    selectedGroups = selectedGroups
                )
            )
        }
    }

    fun updateExerciseState(index: Int, isAdded: Boolean) {
        _uiState.update { currentState ->
            val visibleExercises = currentState.visibleExercises.toMutableList()
            val updatedExercise = visibleExercises[index].copy(
                isAdded = isAdded
            )

            visibleExercises[index] = updatedExercise
            val allExercisesIndex =
                allExercisesState.indexOfFirst { it.exercise.id == updatedExercise.exercise.id }
            allExercisesState[allExercisesIndex] = updatedExercise

            currentState.copy(visibleExercises = visibleExercises)
        }
    }

    fun updateExercisesState(exercises: List<WorkoutExercise>) {
        allExercisesState = allExercisesState.map { state ->
            state.copy(isAdded = exercises.firstOrNull { it.id == state.exercise.id } != null)
        }.toMutableList()

        _uiState.update {
            it.copy(
                visibleExercises = allExercisesState.toList()
            )
        }
    }

    fun onConfirm() {
        navigator.navigateBack()
    }
}
package com.southapps.ui.screen.workout.exercise.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.ui.util.extension.getText
import com.southapps.ui.screen.workout.SharedWorkoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun ExerciseFilter(
    exerciseQuery: String,
    updateExerciseQuery: (String) -> Unit,
    muscleGroupFilter: List<MuscleGroupFilterState>,
    updateMuscleGroupState: (Int) -> Unit
) {
    OutlinedTextField(
        value = exerciseQuery,
        onValueChange = updateExerciseQuery,
        label = { Text("Buscar exercício") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "buscar"
            )
        }
    )

    MuscleGroupChipFilter(muscleGroupFilter, updateMuscleGroupState)
}

@Composable
fun MuscleGroupChipFilter(
    filterList: List<MuscleGroupFilterState>,
    updateMuscleGroupState: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        itemsIndexed(filterList) { index, filterItem ->
            FilterChip(
                selected = filterItem.isSelected,
                onClick = {
                    updateMuscleGroupState(index)
                },
                label = {
                    Text(
                        text = filterItem.filter.getText()
                    )
                }
            )
        }
    }
}

@Composable
private fun ExerciseListItem(
    index: Int,
    exerciseName: String,
    isAdded: Boolean,
    updateExerciseState: (Int, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            updateExerciseState(index, !isAdded)
        }) {
            Icon(
                imageVector = if (isAdded) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isAdded) "Adicionado" else "Adicionar"
            )
        }
    }
}

@Composable
private fun ExerciseList(
    visibleExercises: List<WorkoutExerciseState>,
    updateExerciseState: (Int, Boolean) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        itemsIndexed(visibleExercises) { index, item ->
            ExerciseListItem(
                index,
                item.exercise.name,
                item.isAdded,
                updateExerciseState
            )

            Spacer(Modifier.height(Spacing.sm))

            if (index < visibleExercises.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun WorkoutAddExerciseScreen(viewModel: WorkoutAddExerciseViewModel = koinViewModel(), sharedViewModel: SharedWorkoutViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(sharedUiState.selectedExercises) {
        viewModel.updateExercisesState(sharedUiState.selectedExercises)
    }

    LaunchedEffect(Unit) {
        updateHeader(
            HeaderContent(
                title = "Adicionar exercício",
                showBackButton = true
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.sm),
        ) {
            ExerciseFilter(
                uiState.exerciseQuery,
                viewModel::updateExerciseQuery,
                uiState.muscleGroupFilter,
                viewModel::updateMuscleGroupFilter
            )

            Spacer(Modifier.height(Spacing.md))

            ExerciseList(uiState.visibleExercises, viewModel::updateExerciseState)
        }

        Button(
            onClick = {
                sharedViewModel.updateExercises(viewModel.selectedExercises)
                viewModel.onConfirm()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.sm)
                .fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}
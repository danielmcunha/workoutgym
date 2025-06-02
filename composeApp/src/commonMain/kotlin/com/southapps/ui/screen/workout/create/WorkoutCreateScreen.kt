package com.southapps.ui.screen.workout.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.WorkoutCreateUiAction
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.FormTextField
import com.southapps.core.designSystem.components.StepperField
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.common.validation.form.FormField
import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.domain.workout.entities.WorkoutLocationType
import com.southapps.ui.util.extension.getShortDisplayName
import com.southapps.ui.util.extension.getText
import com.southapps.ui.screen.workout.SharedWorkoutViewModel
import com.southapps.ui.util.TimeVisualTransformation
import kotlinx.datetime.DayOfWeek
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun WorkoutInfoForm(
    name: FormField, onNameChange: (String) -> Unit,
    description: FormField, onDescriptionChange: (String) -> Unit,
    workoutGoal: FormField, onWorkoutGoalChange: (String) -> Unit,
    estimatedDuration: FormField, onEstimatedDurationChange: (String) -> Unit,
    requiredEquipment: FormField,
    onRequiredEquipmentChange: (String) -> Unit,
    masterNotes: FormField,
    onMasterNotesChange: (String) -> Unit,
) {

    FormTextField(
        formField = name,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    Spacer(Modifier.height(Spacing.sm))

    FormTextField(
        formField = description,
        onValueChange = onDescriptionChange,
        singleLine = false,
        modifier = Modifier.fillMaxWidth().height(100.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        maxLines = 4
    )

    Spacer(Modifier.height(Spacing.sm))

    FormTextField(
        formField = workoutGoal,
        onValueChange = onWorkoutGoalChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    Spacer(Modifier.height(Spacing.sm))

    FormTextField(
        formField = estimatedDuration,
        onValueChange = onEstimatedDurationChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        )
    )

    Spacer(Modifier.height(Spacing.sm))

    FormTextField(
        modifier = Modifier.fillMaxWidth(),
        formField = requiredEquipment,
        onValueChange = onRequiredEquipmentChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    Spacer(Modifier.height(Spacing.sm))

    FormTextField(
        modifier = Modifier.fillMaxWidth(),
        formField = masterNotes,
        onValueChange = onMasterNotesChange,
    )
}

@Composable
private fun ExerciseList(exercises: List<WorkoutExercise>?) {
    if (exercises.isNullOrEmpty()) {
        Text(
            text = "Nenhum exercício adicionado",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            exercises.forEachIndexed { index, exercise ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = exercise.muscleGroup.getText(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "${exercise.series.size} x ${exercise.series.firstOrNull()?.name ?: "-"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (index != exercises.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.sm),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCreateScreen(
    viewModel: WorkoutCreateViewModel = koinViewModel(),
    sharedViewModel: SharedWorkoutViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose {

        }
    }

    LaunchedEffect(viewModel.uiAction) {
        viewModel.uiAction.collect { action ->
            if (action is WorkoutCreateUiAction.UpdateExercises) {
                sharedViewModel.updateExercises(action.exercises)
            }
        }
    }

    LaunchedEffect(Unit) {
        updateHeader(
            HeaderContent(
                title = "Cadastrar treino",
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.sm)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WorkoutInfoForm(
            name = uiState.workoutName,
            onNameChange = viewModel::updateWorkoutName,
            description = uiState.description,
            onDescriptionChange = viewModel::updateDescription,
            workoutGoal = uiState.workoutGoal,
            onWorkoutGoalChange = viewModel::updateWorkoutGoal,
            estimatedDuration = uiState.estimatedDuration,
            onEstimatedDurationChange = viewModel::updateEstimatedDuration,
            requiredEquipment = uiState.requiredEquipment,
            onRequiredEquipmentChange = viewModel::updateRequiredEquipment,
            masterNotes = uiState.masterNotes,
            onMasterNotesChange = viewModel::updateMasterNotes
        )

        Spacer(Modifier.height(Spacing.md))

        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Exercícios",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(Spacing.xs))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExerciseList(sharedUiState.selectedExercises)

                Spacer(Modifier.height(Spacing.md))

                OutlinedButton(onClick = {
                    viewModel.addExercise()
                }) {
                    Text("+ Adicionar exercício")
                }
            }
        }

        if (uiState.error?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(6.dp)
            )
        }

        Spacer(Modifier.height(Spacing.md))

        Button(
            onClick = {
                viewModel.saveWorkout(sharedUiState.selectedExercises)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text("Salvar")
        }
    }
}


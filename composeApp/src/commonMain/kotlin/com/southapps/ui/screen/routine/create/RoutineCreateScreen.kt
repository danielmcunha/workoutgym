package com.southapps.ui.screen.routine.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.FormTextField
import com.southapps.core.designSystem.components.StepperField
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.workout.entities.WorkoutLocationType
import com.southapps.domain.workout.entities.WorkoutSummary
import com.southapps.ui.util.TimeVisualTransformation
import com.southapps.ui.util.extension.getShortDisplayName
import com.southapps.ui.util.extension.getText
import kotlinx.datetime.DayOfWeek
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoutineCreateScreen(viewModel: RoutineCreateViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        updateHeader(HeaderContent(title = "Novo Compromisso", showBackButton = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        WorkoutSelector(
            workouts = uiState.workouts,
            selectedWorkout = uiState.selectedWorkout.value as? WorkoutSummary,
            updateSelectedWorkout = viewModel::updateSelectedWorkout
        )

        FrequencySection(
            frequencyType = uiState.frequencyType.value as FrequencyType,
            daysOfWeek = uiState.daysOfWeek.value as Set<DayOfWeek>,
            timesPerWeek = uiState.timesPerWeek.value as Int,
            updateFrequencyType = viewModel::updateFrequencyType,
            toggleDayOfWeek = viewModel::toggleDayOfWeek,
            updateTimesPerWeek = viewModel::updateTimesPerWeek
        )

        ModalitySection(
            isPresential = uiState.isPresential.value as Boolean,
            updateIsPresential = viewModel::updateIsPresential
        )

        LocationSection(
            locationType = uiState.locationType.value as? WorkoutLocationType,
            locationDetails = uiState.locationDetails.value.toString(),
            updateLocationType = viewModel::updateLocationType,
            updateLocationDetails = viewModel::updateLocationDetails
        )

        FormTextField(
            modifier = Modifier.fillMaxWidth(),
            formField = uiState.scheduledTime,
            onValueChange = viewModel::updateScheduledTime,
            visualTransformation = TimeVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("10:30") },
        )

        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(Spacing.lg))

        Button(
            onClick = { viewModel.saveRoutine() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar compromisso")
        }
    }
}

@Composable
fun WorkoutSelector(
    workouts: List<WorkoutSummary>,
    selectedWorkout: WorkoutSummary?,
    updateSelectedWorkout: (WorkoutSummary) -> Unit
) {
    Column {
        Text("Treino", style = MaterialTheme.typography.titleSmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            workouts.forEach { workout ->
                FilterChip(
                    selected = workout == selectedWorkout,
                    onClick = { updateSelectedWorkout(workout) },
                    label = { Text(workout.name) }
                )
            }
        }
    }
}

@Composable
fun FrequencySection(
    frequencyType: FrequencyType,
    daysOfWeek: Set<DayOfWeek>,
    timesPerWeek: Int,
    updateFrequencyType: (FrequencyType) -> Unit,
    toggleDayOfWeek: (DayOfWeek) -> Unit,
    updateTimesPerWeek: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        FrequencyType.entries.forEachIndexed { index, type ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index, FrequencyType.entries.size),
                onClick = { updateFrequencyType(type) },
                selected = type == frequencyType
            ) { Text(type.getText()) }
        }
    }

    when (frequencyType) {
        FrequencyType.DAYS_OF_WEEK -> {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                DayOfWeek.entries.forEach { day ->
                    FilterChip(
                        selected = daysOfWeek.contains(day),
                        onClick = { toggleDayOfWeek(day) },
                        label = { Text(day.getShortDisplayName()) }
                    )
                }
            }
        }

        FrequencyType.TIMES_PER_WEEK -> {
            StepperField(
                label = "Vezes por semana",
                value = timesPerWeek,
                onValueChange = updateTimesPerWeek,
                modifier = Modifier.fillMaxWidth(),
                min = 1,
                max = 7
            )
        }
    }
}

@Composable
fun ModalitySection(
    isPresential: Boolean,
    updateIsPresential: (Boolean) -> Unit
) {
    val options = listOf("Presencial", "Online")
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index, options.size),
                onClick = { updateIsPresential(label == "Presencial") },
                selected = (label == "Presencial") == isPresential
            ) { Text(label) }
        }
    }
}

@Composable
fun LocationSection(
    locationType: WorkoutLocationType?,
    locationDetails: String,
    updateLocationType: (WorkoutLocationType) -> Unit,
    updateLocationDetails: (String) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        WorkoutLocationType.entries.forEach { type ->
            FilterChip(
                selected = type == locationType,
                onClick = { updateLocationType(type) },
                label = { Text(type.getText()) }
            )
        }
    }

    if (locationType != null) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = locationDetails,
            onValueChange = updateLocationDetails,
            label = {
                Text(
                    when (locationType) {
                        WorkoutLocationType.ONLINE_CALL -> "Link da chamada"
                        WorkoutLocationType.OTHER -> "Especifique o local"
                        else -> "Endereço"
                    }
                )
            },
        )
    }
}
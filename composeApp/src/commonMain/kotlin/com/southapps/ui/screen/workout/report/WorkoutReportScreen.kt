package com.southapps.ui.screen.workout.report

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.charts.BarLineChart
import com.southapps.core.designSystem.components.charts.BarLineData
import com.southapps.core.designSystem.components.charts.BarSeries
import com.southapps.core.designSystem.components.charts.BarSeriesData
import com.southapps.core.designSystem.components.charts.LineSeries
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.extensions.toShortDateTime
import com.southapps.domain.workout.entities.WorkoutReport
import com.southapps.domain.workout.entities.WorkoutProgress
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutReportScreen(viewModel: WorkoutReportViewModel = koinViewModel()) {
    val updateHeader = LocalHeaderContent.current
    val register = LocalUIActionRegister.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val report = uiState.workoutReport

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose { }
    }

    LaunchedEffect(Unit) {
        updateHeader(
            HeaderContent(
                title = "Relatório de treino",
                showBackButton = true,
                icons = listOf(

                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        report?.let {
            WorkoutInfoCard(it)
            Spacer(modifier = Modifier.height(Spacing.md))
            WorkoutResumeTabs(it) {
                viewModel.onDeleteItemClick(it)
            }
        }

    }
}

@Composable
private fun WorkoutResumeTabs(
    report: WorkoutReport,
    onDeleteClick: (String) -> Unit,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Exercícios", "Treinos")

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        tabs.forEachIndexed { index, title ->
            TabButton(
                text = title,
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
            )
        }
    }

    Spacer(modifier = Modifier.height(Spacing.md))

    when (selectedTabIndex) {
        0 -> ExercisesReport(report)
        1 -> WorkoutsReport(report.workouts, onDeleteClick)
    }
}

@Composable
private fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(
            onClick = { onClick() },
            modifier = Modifier.weight(1f)
        ) {
            Text(text)
        }

    } else {
        OutlinedButton(
            onClick = { onClick() },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ExercisesReport(
    report: WorkoutReport,
) {
    val options = report.workouts.lastOrNull()?.exercises?.map { it.name } ?: listOf()
    var selectedOption by remember { mutableStateOf(0) }

    val barSeries = mutableListOf<BarSeriesData>()
    val lineSeries = mutableListOf<Float>()

    report.workouts.forEach { workout ->
        workout.exercises[selectedOption].let { exercise ->
            barSeries.add(
                BarSeriesData(
                    exercise.series.lastOrNull()?.value?.toFloatOrNull() ?: 0f,
                    workout.shortDate
                )
            )
            lineSeries.add(exercise.series.lastOrNull()?.name?.toFloatOrNull() ?: 0f)
        }
    }

    ExerciseSelectorDropDown(options) {
        selectedOption = it
    }

    BarLineChart(
        data = BarLineData(
            barSeries = BarSeries(
                values = barSeries,
                colorSelected = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorScheme.primaryContainer
            ),
            lineSeries = LineSeries(
                lineSeries,
                Color.Gray
            )
        ),
        modifier = Modifier
            .padding(vertical = Spacing.md)
            .fillMaxWidth()
            .height(220.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseSelectorDropDown(options: List<String>, onItemSelected: (Int) -> Unit) {
    if(options.isEmpty()) return
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(horizontal = Spacing.sm)
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Exercício selecionado") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { i, selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false

                        onItemSelected(i)
                    }
                )
            }
        }
    }
}

@Composable
private fun WorkoutsReport(
    workoutProgressList: List<WorkoutProgress>,
    onDeleteClick: (String) -> Unit,
) {
    workoutProgressList.forEach { workout ->
        Card(
            modifier = Modifier.padding(
                horizontal = Spacing.sm,
                vertical = Spacing.xs
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    top = Spacing.md,
                    start = Spacing.md,
                    bottom = Spacing.md,
                    end = Spacing.xs
                )
            ) {
                WorkoutItem(
                    label = "Data",
                    value = workout.startTime.toShortDateTime(),
                    modifier = Modifier
                        .weight(1.2f)
                )

                WorkoutItem(
                    label = "Duração",
                    value = workout.duration,
                    modifier = Modifier
                        .weight(.8f)
                )

                WorkoutItem(
                    label = "Exercícios",
                    value = workout.exercisesCompletion,
                    modifier = Modifier
                        .weight(.65f)
                )

                IconButton(onClick = {
                    onDeleteClick("")
                }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutItem(
    label: String, value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f),
            textAlign = TextAlign.Start,
        )

        Text(
            value,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
private fun WorkoutInfoCard(workoutReport: WorkoutReport) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
            .animateContentSize(),
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            WorkoutInfoCardHeader("Nome do treino", workoutReport.workoutName)
            WorkoutInfoCardRow(
                "Último treino", workoutReport.lastWorkoutDate,
                "Duração média", workoutReport.lastWorkoutDuration
            )
            WorkoutInfoCardRow(
                "Treinos", workoutReport.sequenceCount.toString(),
                "% conclusão média", "${workoutReport.completionAverage} %"
            )
        }

    }
}

@Composable
private fun WorkoutInfoCardHeader(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Icon(
            imageVector = Icons.Default.FitnessCenter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        WorkoutInfoItem(
            label,
            value,
            small = false
        )
    }
}

@Composable
private fun WorkoutInfoCardRow(
    label1: String,
    value1: String,
    label2: String,
    value2: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        WorkoutInfoItem(
            label1,
            value1,
            modifier = Modifier.weight(1f)
        )

        WorkoutInfoItem(
            label2,
            value2,
            modifier = Modifier.weight(.7f)
        )
    }
}

@Composable
private fun WorkoutInfoItem(
    label: String, value: String,
    modifier: Modifier = Modifier,
    small: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f),
        )

        Text(
            text = value,
            style = if (small) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
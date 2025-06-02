package com.southapps.ui.screen.workout.active

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.HeaderIcon
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.components.FlashingText
import com.southapps.core.designSystem.components.Timer
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.domain.workout.entities.WorkoutExerciseProgress
import com.southapps.domain.workout.entities.WorkoutExerciseSeriesProgress
import com.southapps.domain.workout.entities.WorkoutProgress
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutScreen(viewModel: WorkoutActiveViewModel = koinViewModel()) {
    val updateHeader = LocalHeaderContent.current
    val register = LocalUIActionRegister.current

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Exercício Atual", "Lista de Exercícios")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        viewModel.checkCurrentWorkout()

        onDispose { }
    }

    LaunchedEffect(Unit) {
        updateHeader(
            HeaderContent(
                title = uiState.workout.name,
                showBackButton = true,
                icons = listOf(
                    HeaderIcon(
                        icon = Icons.Default.DoneAll,
                        action = { viewModel.completeWorkout() }
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            WorkoutInfo(uiState.workout, uiState.currentExercise)

            Spacer(modifier = Modifier.height(Spacing.sm))

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> CurrentExerciseTab(uiState.currentExercise) { index, state ->
                    viewModel.onSerieStateChanged(
                        index,
                        state
                    )
                }

                1 -> ExercisesListTab(uiState.currentExercise, uiState.workout.exercises) {
                    viewModel.onExerciseSelected(it)
                    selectedTabIndex = 0
                }

            }
        }

        if (selectedTabIndex == 0) {
            WorkoutControl(
                uiState.nextExerciseName,
                uiState.exerciseStatePosition,
                { viewModel.nextExercise() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        SlidingDrawer(uiState.workout.startTime)
        //FloatingTimer()
    }
}

@Composable
fun SlidingDrawer(workoutStartTime: Long) {
    var isExpanded by remember { mutableStateOf(false) }

    val width by animateDpAsState(
        targetValue = if (isExpanded) 160.dp else 32.dp,
        animationSpec = tween(durationMillis = 300),
        label = "drawerWidth"
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            modifier = Modifier
                .width(width)
                .height(48.dp)
                .background(
                    MaterialTheme.colorScheme.primary, RoundedCornerShape(
                        topStart = 4.dp, bottomStart = 4.dp
                    )
                )
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = Spacing.sm, horizontal = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = if (isExpanded)
                    Icons.AutoMirrored.Filled.KeyboardArrowRight
                else
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Toggle Drawer"
            )

            if (isExpanded) {
                Timer(
                    (currentTime().timeInMillis() - workoutStartTime),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun WorkoutInfo(workout: WorkoutProgress, currentExercise: WorkoutExerciseProgress) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing.md)
    ) {
        Text(
            text = "Exercício atual",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f),
        )

        FlashingText(
            text = currentExercise.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = Spacing.xs)
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        Text(
            text = "Progresso do treino",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .8f),
        )

        LinearProgressIndicator(
            progress = { workout.progress.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.sm)
                .height(8.dp)
        )
    }
}

@Composable
private fun ExerciseNote(note: String?) {
    if (note.isNullOrEmpty()) return

    Card(
        modifier = Modifier.padding(
            top = Spacing.md,
            start = Spacing.sm,
            end = Spacing.sm
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null
            )

            Text(note)
        }
    }
}

@Composable
private fun CurrentExerciseTab(
    currentExercise: WorkoutExerciseProgress,
    onSerieStateChanged: (Int, Boolean) -> Unit,
) {
    ExerciseNote(currentExercise.note)
    CurrentSeries(currentExercise.series, onSerieStateChanged)
}


@Composable
private fun WorkoutControl(
    nextExercise: String?,
    exerciseState: String,
    onNextExerciseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        )
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Spacing.sm,
                    vertical = Spacing.md
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small
                    )
                    .height(44.dp)
                    .padding(
                        vertical = 2.dp,
                        horizontal = Spacing.md
                    )
            ) {
                Text(exerciseState)
            }

            CustomButton(
                text = if (nextExercise != null) "Próximo: $nextExercise" else "Finalizar treino",
                onClick = { onNextExerciseClick() },
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Composable
private fun CurrentSeries(
    series: List<WorkoutExerciseSeriesProgress>,
    onSerieStateChanged: (Int, Boolean) -> Unit,
) {
    ExerciseSeriesList(
        series,
        onSerieStateChanged
    )
}

@Composable
private fun ExerciseSeriesList(
    series: List<WorkoutExerciseSeriesProgress>,
    onSerieStateChanged: (Int, Boolean) -> Unit,
) {
    val hideExerciseLoad = series.any { it.value.isNullOrEmpty() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            vertical = Spacing.md
        )
    ) {
        Text(
            "SÉRIE",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(70.dp)
        )

        Text(
            "REPS",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
        )

        if (!hideExerciseLoad) {
            Text(
                "CARGA",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }

        Icon(
            Icons.Default.Checklist, contentDescription = null,
            modifier = Modifier
                .padding(horizontal = Spacing.md)
                .size(24.dp)
        )
    }

    HorizontalDivider()

    series.forEachIndexed { i, serie ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                vertical = Spacing.sm
            )
        ) {
            Text(
                (i + 1).toString(),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(70.dp)
            )

            Text(
                serie.name,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )

            if (!hideExerciseLoad) {
                Text(
                    serie.value.orEmpty(),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }

            Checkbox(
                checked = serie.done,
                onCheckedChange = { onSerieStateChanged(i, it) },
                modifier = Modifier
                    .padding(horizontal = Spacing.md)
                    .size(24.dp)
            )
        }
    }
}

@Composable
private fun ExercisesListTab(
    currentExercise: WorkoutExerciseProgress,
    exercises: List<WorkoutExerciseProgress>,
    onExerciseSelected: (WorkoutExerciseProgress) -> Unit,
) {
    Spacer(modifier = Modifier.height(Spacing.sm))

    exercises.forEachIndexed { index, exercise ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs)
                .clickable { onExerciseSelected(exercise) },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Row {
                        Text(
                            text = exercise.category,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.height(Spacing.xs))
                        Text(
                            text = "${exercise.series.size} x ${exercise.series.first().name}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(Spacing.sm)
                        .clip(CircleShape)
                        .background(
                            when {
                                exercise.done -> Color.Green
                                exercise == currentExercise -> MaterialTheme.colorScheme.primary
                                else -> Color.Gray
                            }
                        )
                )
            }
        }
    }
}

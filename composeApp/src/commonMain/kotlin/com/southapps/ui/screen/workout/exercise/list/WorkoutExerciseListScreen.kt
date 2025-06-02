package com.southapps.ui.screen.workout.exercise.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.StepperField
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.ui.screen.workout.SharedWorkoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutExerciseListScreen(
    viewModel: WorkoutExerciseListViewModel = koinViewModel(),
    sharedViewModel: SharedWorkoutViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val updateHeader = LocalHeaderContent.current

    var orderedExercises: List<WorkoutExercise> by remember(sharedUiState.selectedExercises) {
        mutableStateOf(sharedUiState.selectedExercises.toMutableList())
    }

    var isOrderingMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        updateHeader(HeaderContent(title = "Exercícios do treino", showBackButton = true))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (sharedUiState.selectedExercises.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(
                        onClick = {
                            sharedViewModel.updateExerciseOrder(orderedExercises)
                            isOrderingMode = !isOrderingMode
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Reorder,
                            contentDescription = if (isOrderingMode) "Concluir ordenação" else "Ordenar exercícios"
                        )
                    }
                }
            }

            WorkoutExerciseListContent(
                exercises = if (isOrderingMode) orderedExercises else sharedUiState.selectedExercises,
                isOrderingMode = isOrderingMode,
                onUpdateSeries = sharedViewModel::updateExerciseSeries,
                onUpdateReps = sharedViewModel::updateExerciseReps,
                onOrderChanged = {
                    orderedExercises = it
                }
            )

            Spacer(Modifier.height(Spacing.md))

            if (!isOrderingMode) {
                Button(
                    onClick = {
                        sharedViewModel.updateExercises(sharedUiState.selectedExercises)
                        viewModel.onAddExercises()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Adicionar exercícios")
                }
            }
        }

        Button(
            onClick = {
                viewModel.onSave()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.sm)
                .fillMaxWidth()
        ) {
            Text("Salvar exercícios")
        }
    }
}

@Composable
fun DraggableExerciseListContent(
    exercises: List<WorkoutExercise>,
    onOrderChanged: (List<WorkoutExercise>) -> Unit
) {
    var orderedExerciseList by remember {
        mutableStateOf(exercises.toMutableList())
    }
    val stateList = rememberLazyListState()
    var draggingItemDeltaY: Float by remember { mutableFloatStateOf(0f) }
    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    val onMove = { fromIndex: Int, toIndex: Int ->
        orderedExerciseList =
            orderedExerciseList.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    LaunchedEffect(orderedExerciseList) {
        onOrderChanged(orderedExerciseList)
    }

    LazyColumn(
        modifier = Modifier.pointerInput(key1 = stateList) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    stateList.layoutInfo.visibleItemsInfo
                        .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                        ?.let { item ->
                            draggingItemIndex = item.index
                        }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    draggingItemDeltaY += dragAmount.y

                    val fromIndex = draggingItemIndex ?: return@detectDragGesturesAfterLongPress
                    val draggingItem = stateList.layoutInfo.visibleItemsInfo
                        .firstOrNull { it.index == fromIndex }
                        ?: return@detectDragGesturesAfterLongPress

                    val startOffset = draggingItem.offset + draggingItemDeltaY
                    val endOffset =
                        draggingItem.offset + draggingItem.size + draggingItemDeltaY
                    val midOffset = startOffset + (endOffset - startOffset) / 2

                    val targetItem =
                        stateList.layoutInfo.visibleItemsInfo.find { item ->
                            midOffset.toInt() in item.offset..item.offset + item.size &&
                                    draggingItem.index != item.index
                        }

                    if (targetItem != null) {
                        onMove(fromIndex, targetItem.index)
                        draggingItemIndex = targetItem.index
                        draggingItemDeltaY += draggingItem.offset - targetItem.offset
                    }

                },
                onDragEnd = {
                    draggingItemIndex = null
                    draggingItemDeltaY = 0f
                },
                onDragCancel = {
                    draggingItemIndex = null
                    draggingItemDeltaY = 0f
                },
            )
        },
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        state = stateList
    ) {
        itemsIndexed(orderedExerciseList) { index, item ->
            val modifier = Modifier.then(
                if (draggingItemIndex == index) Modifier
                    .zIndex(1f)
                    .graphicsLayer { translationY = draggingItemDeltaY }
                else Modifier
            )

            ExerciseDraggableCard(modifier, index, item)
        }
    }
}

@Composable
fun WorkoutExerciseListContent(
    exercises: List<WorkoutExercise>,
    isOrderingMode: Boolean,
    onUpdateSeries: (Int, Int) -> Unit,
    onUpdateReps: (Int, Int) -> Unit,
    onOrderChanged: (List<WorkoutExercise>) -> Unit
) {
    if (exercises.isEmpty()) {
        Text("Nenhum exercício adicionado ainda.")
    } else {
        if (isOrderingMode) {
            DraggableExerciseListContent(exercises, onOrderChanged)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                itemsIndexed(exercises) { index, item ->
                    ExerciseEditableCard(
                        index = index,
                        exercise = item,
                        series = item.series.count(),
                        reps = item.series.firstOrNull()?.name?.toIntOrNull() ?: 0,
                        updateSeries = onUpdateSeries,
                        updateReps = onUpdateReps
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseEditableCard(
    index: Int,
    exercise: WorkoutExercise,
    series: Int,
    reps: Int,
    updateSeries: (Int, Int) -> Unit,
    updateReps: (Int, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                StepperField(
                    label = "Séries",
                    value = series,
                    onValueChange = { updateSeries(index, it) },
                    modifier = Modifier.fillMaxWidth()
                )
                StepperField(
                    label = "Reps",
                    value = reps,
                    onValueChange = { updateReps(index, it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ExerciseDraggableCard(modifier: Modifier, index: Int, exercise: WorkoutExercise) {
    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = Icons.Default.DragHandle,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Ordenar"
            )
        }
    }
}
package com.southapps.ui.screen.master.chicken.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.HeaderIcon
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.InfoTag
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.workout.entities.WorkoutSummary
import com.southapps.ui.screen.master.components.ChickenCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ChickenDetailScreen(
    viewModel: ChickenDetailViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val uiState by viewModel.uiState.collectAsState()
    val sharedContentState = rememberSharedContentState(key = uiState.key)
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        register(viewModel.uiAction)
        updateHeader(
            HeaderContent(
                title = "Detalhe", showBackButton = true,
            icons = listOf(
                HeaderIcon(Icons.Default.Delete) { viewModel.removeChicken() }
            )
        ))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.sm)
    ) {
        ChickenCard(
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    sharedContentState = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            chicken = uiState.summary,
            expanded = true
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        ChickenWorkoutsSection(
            workouts = uiState.summary.workouts,
            onEdit = viewModel::onEditWorkout,
            onAdd = viewModel::onAddWorkout,
            onScheduleWorkout = viewModel::onScheduleWorkout
        )
    }
}

@Composable
private fun ChickenWorkoutsSection(
    workouts: List<WorkoutSummary>,
    onEdit: (String) -> Unit,
    onScheduleWorkout: () -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Treinos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        AssistChip(
            onClick = onScheduleWorkout,
            label = { Text("Rotina do Aluno") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(Spacing.md))

    if (workouts.isEmpty()) {
        Text(
            text = "Nenhum treino cadastrado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = Spacing.md)
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            workouts.forEach { workout ->
                WorkoutSummaryCard(workout = workout) {
                    onEdit(workout.workoutReference)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(Spacing.lg))

    Button(
        onClick = onAdd,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Add, contentDescription = "Novo treino")
        Spacer(modifier = Modifier.width(Spacing.sm))
        Text("Adicionar Novo Treino")
    }
}

@Composable
fun WorkoutSummaryCard(
    workout: WorkoutSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(Spacing.md)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Ícone de Treino",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.sm))
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            workout.workoutGoal?.let { goal ->
                InfoTag(
                    icon = Icons.Filled.RadioButtonChecked,
                    text = goal,
                    iconSize = 16.dp
                )
            }
        }
    }
}
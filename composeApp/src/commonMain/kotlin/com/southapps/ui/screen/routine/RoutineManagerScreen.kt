import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OnlinePrediction
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.InfoTag
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.workout.entities.FrequencyType
import com.southapps.domain.routine.entities.WorkoutRoutine
import com.southapps.domain.routine.entities.formattedScheduledTime
import com.southapps.ui.util.extension.getShortDisplayName
import com.southapps.ui.util.extension.getText
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutScheduleScreen(
    viewModel: WorkoutRoutineManagerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        updateHeader(HeaderContent(title = "Rotina de Treinos", showBackButton = true))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Spacing.sm, vertical = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            if (uiState.routine.isEmpty()) {
                item {
                    Text(
                        text = "Nenhum compromisso cadastrado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                itemsIndexed(uiState.routine) { index, routine ->
                    WorkoutRoutineCard(
                        routine = routine,
                        onEdit = { viewModel.onEditRoutine(routine) },
                        onDelete = { viewModel.removeRoutine(index, routine.uid.orEmpty()) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        ExtendedFloatingActionButton(
            onClick = { viewModel.addRoutine() },
            text = { Text("Adicionar Rotina") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.md)
        )
    }
}

@Composable
fun WorkoutRoutineCard(
    routine: WorkoutRoutine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = routine.workoutSummary?.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar compromisso",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover compromisso",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                InfoTag(
                    icon = Icons.Default.CalendarToday,
                    text = when (routine.frequencyType) {
                        FrequencyType.DAYS_OF_WEEK -> routine.daysOfWeek
                            .joinToString(", ") { d -> d.getShortDisplayName() }

                        FrequencyType.TIMES_PER_WEEK -> "${routine.timesPerWeek}x/semana"
                    }
                )

                InfoTag(icon = Icons.Default.Schedule, text = routine.formattedScheduledTime)

                InfoTag(
                    icon = if (routine.isPresential) Icons.Default.People else Icons.Default.OnlinePrediction,
                    text = if (routine.isPresential) "Presencial" else "Online"
                )

                routine.locationType?.let {
                    InfoTag(icon = Icons.Default.LocationOn, text = it.getText())
                }

                routine.locationDetails?.takeIf { it.isNotBlank() }?.let {
                    InfoTag(icon = Icons.Default.LocationOn, text = it)
                }
            }
        }
    }
}
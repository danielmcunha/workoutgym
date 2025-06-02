package com.southapps.ui.screen.workout.current

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.tokens.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutCurrentComponent(viewModel: WorkoutCurrentComponentViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose { }
    }

    LaunchedEffect(true) {
        viewModel.fetch()
    }

    AnimatedVisibility(
        uiState.value.workoutProgress != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md)
                    .animateContentSize()
        ) {
            uiState.value.workoutProgress?.let {
                Column(
                    modifier = Modifier.padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WorkoutInfoCardHeader(
                            "Treino em andamento",
                            it.name
                        )

                        IconButton(onClick = {
                            viewModel.onCancelCurrentWorkout()
                        }) {
                            Icon(
                                Icons.Default.Close,
                                "Fechar"
                            )
                        }
                    }


                    Button(
                        onClick = {
                            viewModel.onContinueCurrentWorkout()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retomar treino")
                    }
                }
            }
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
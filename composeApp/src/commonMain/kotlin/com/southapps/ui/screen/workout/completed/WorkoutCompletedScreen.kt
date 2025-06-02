package com.southapps.ui.screen.workout.completed

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.extensions.formatTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutSuccessScreen(viewModel: WorkoutSuccessViewModel = koinViewModel()) {
    val updateHeader = LocalHeaderContent.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        updateHeader(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(Spacing.sm)
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.lg)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success Icon",
                        modifier = Modifier.size(132.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Treino completo!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(Spacing.xs))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⌛ Tempo de treino:",
                                color = Color(0xFFB0BEC5)
                            )
                            Text(
                                text = uiState.value.workoutCompleted.duration.formatTime(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🏋 Exercícios concluidos:",
                                color = Color(0xFFB0BEC5)
                            )
                            Text(
                                text = uiState.value.workoutCompleted.exercisesCompleted.toString(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🔥 sequência de treino:",
                                color = Color(0xFFB0BEC5)
                            )
                            Text(
                                text = "${uiState.value.workoutCompleted.sequenceCount}x",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {

                Card(
                    modifier = Modifier
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(Spacing.sm)
                ) {
                    Text(
                        text = "Compartilhar",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                            .padding(Spacing.md)
                            .clickable {
                                viewModel.share()
                            }
                    )
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            viewModel.finish()
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(Spacing.sm)
                ) {
                    Text(
                        text = "Finalizar",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                            .padding(Spacing.md)
                    )
                }
            }
        }

    }
}
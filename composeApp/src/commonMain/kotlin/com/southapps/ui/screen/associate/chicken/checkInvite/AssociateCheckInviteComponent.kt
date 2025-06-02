package com.southapps.ui.screen.associate.chicken.checkInvite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AssociateCheckInviteComponent(viewModel: AssociateCheckInviteViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose { }
    }

    AnimatedVisibility(
        uiState.value.isVisible == true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TrainerInviteCard(
            trainerName = uiState.value.masterSummary?.name.orEmpty(),
            onAccept = { viewModel.acceptInvite() },
            onDecline = { viewModel.declineInvite() },
            isAcceptLoading = uiState.value.isAcceptLoading == true,
            isDeclineLoading = uiState.value.isDeclineLoading == true,
        )
    }
}

@Composable
fun TrainerInviteCard(
    trainerName: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isAcceptLoading: Boolean = false,
    isDeclineLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .1f)
    val borderColor = MaterialTheme.colorScheme.primaryContainer
    val pulseAnim = rememberInfiniteTransition()
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Convite de associação",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$trainerName quer se associar a você como treinador.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aceitar")
                }
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Recusar")
                }
            }
        }
    }
}

package com.southapps.ui.screen.associate.master.listInvites

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.HeaderIcon
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.extensions.toShortDateTime
import com.southapps.domain.associate.entities.AssociateInvite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AssociateListInvitesScreen(viewModel: AssociateListInvitesViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        register(viewModel.uiAction)
        updateHeader(
            HeaderContent(
                title = "Convites em aberto",
                showBackButton = true,
                icons = listOf(
                    HeaderIcon(
                        Icons.Default.Add
                    ) { viewModel.sendNewInvite() })
            )
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!uiState.invites.isNullOrEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.invites!!, key = { it.chickenEmail }) { invite ->
                InviteCard(
                    invite = invite,
                    onCancel = { viewModel.onCancelClick(invite.id.orEmpty()) }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Você não tem nenhum convite em aberto!")
        }
    }
}

@Composable
private fun InviteCard(invite: AssociateInvite, onCancel: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Enviado para:", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = invite.chickenEmail, style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enviado em: ${invite.inviteCreatedAt.toShortDateTime()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.width(Spacing.sm))
            OutlinedButton(
                onClick = onCancel,
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
            }
        }
    }
}
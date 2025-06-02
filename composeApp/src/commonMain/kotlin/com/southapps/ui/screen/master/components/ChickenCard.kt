package com.southapps.ui.screen.master.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.southapps.core.designSystem.components.InfoTag
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.chicken.entities.ChickenSummary

@Composable
fun ChickenCard(
    modifier: Modifier,
    chicken: ChickenSummary,
    expanded: Boolean,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = {
            onClick?.let { it() }
        }
    ) {
        ChickenCardContent(expanded, chicken)
    }
}

@Composable
private fun ChickenCardContent(expanded: Boolean, chicken: ChickenSummary) {
    Column(modifier = Modifier.padding(Spacing.md)) {
        ChickenHeader(chicken)
        Spacer(modifier = Modifier.height(Spacing.sm))
        ChickenInfoRow(chicken)

        if (!expanded && chicken.workouts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing.md))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = "Número de treinos",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text(
                    text = "${chicken.workouts.size} treino(s) cadastrado(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(Spacing.md))
            ChickenAdditionalInfo(chicken)
        }
    }
}

@Composable
private fun ChickenHeader(chicken: ChickenSummary) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Aluno",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(Spacing.md))
        Column {
            Text(
                text = chicken.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = chicken.goal?.mainGoal.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
private fun ChickenInfoRow(chicken: ChickenSummary) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxWidth()
    ) {
        chicken.age?.let { InfoTag(text = "${it} anos") }
        chicken.bodyInfo?.let {
            InfoTag(text = "${it.weight} kg")
            InfoTag(text = "${it.height} cm")
        }
    }
}

@Composable
private fun ChickenAdditionalInfo(chicken: ChickenSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        InfoLine(label = "E-mail", value = chicken.email)
        chicken.phone?.let { InfoLine(label = "Telefone", value = it) }
        chicken.note?.let { InfoLine(label = "Observações", value = it) }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
    }
}
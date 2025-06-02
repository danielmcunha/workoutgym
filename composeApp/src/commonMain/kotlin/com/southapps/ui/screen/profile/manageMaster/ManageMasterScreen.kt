package com.southapps.ui.screen.profile.manageMaster

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.domain.master.entities.MasterSummary
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ManageMasterScreen(
    viewModel: ManageMasterViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedContentState = rememberSharedContentState(key = uiState.sharedTransitionKey)
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        register(viewModel.uiAction)
        updateHeader(
            HeaderContent(
                title = "Gerenciar treinador",
                showBackButton = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        TrainerCard(uiState.masterSummary,
            modifier = Modifier
                .sharedElement(
                    sharedContentState = sharedContentState,
                    animatedVisibilityScope = animatedVisibilityScope
                ))
        Options(viewModel::removeTrainer)
    }
}

@Composable
private fun TrainerCard(masterSummary: MasterSummary?,
                        modifier: Modifier) {
    if (masterSummary == null) return

    Card(
        shape = RoundedCornerShape(Spacing.md),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp),
                    color = Color.Gray
                ) {
                    Icon(
                        Icons.Default.Person, contentDescription = null,
                        modifier = Modifier.padding(Spacing.sm),
                        tint = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(masterSummary.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Personal Trainer", color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Email", color = Color.Gray)
            Text(masterSummary.email, color = Color.White)
            masterSummary.phone?.let { phone ->
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text("Phone", color = Color.Gray)
                Text(phone, color = Color.White)
            }
        }
    }
}

@Composable
private fun Options(
    removerTrainer: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(Spacing.md),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
        ) {
            OptionButton("Whatsapp", Icons.Default.Whatsapp) { }
            HorizontalDivider()
            OptionButton("Instagram", Icons.Default.Camera) { }
            HorizontalDivider()
            OptionButton("Youtube", Icons.Default.YoutubeSearchedFor) { }
            HorizontalDivider()
            OptionButton("Remover treinador", Icons.Default.Delete, removerTrainer)
        }
    }
}

@Composable
private fun OptionButton(name: String, icon: ImageVector, action: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { action.invoke() }
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(Spacing.sm))
        Text(name, color = Color.White)
    }
}
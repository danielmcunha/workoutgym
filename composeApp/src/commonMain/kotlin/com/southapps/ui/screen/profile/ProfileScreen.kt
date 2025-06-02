package com.southapps.ui.screen.profile

import androidx.compose.animation.AnimatedContentScope
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.extensions.dateToLongDate
import com.southapps.domain.master.entities.MasterSummary
import com.southapps.domain.user.entities.User
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedContentScope,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    val sharedContentState =
        rememberSharedContentState(key = uiState.masterSummary?.masterId.orEmpty())

    LaunchedEffect(Unit) {
        register(viewModel.uiAction)
        updateHeader(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.md)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        Spacer(Modifier.height(Spacing.sm))
        UserInfo(uiState.user)
        BeATrainerBanner(uiState.showBeATrainer)
        ProfileOptions()
        FindATrainer(uiState.showFindATrainer)
        TrainerCard(
            uiState.masterSummary,
            modifier = Modifier.sharedElement(
                sharedContentState = sharedContentState,
                animatedVisibilityScope = animatedVisibilityScope
            ),
            viewModel::onManageMasterClick
        )
        Logout(viewModel::logOut)
        Spacer(Modifier.height(Spacing.sm))

    }
}

@Composable
private fun UserInfo(user: User?) {
    if (user == null) return

    Card(
        shape = RoundedCornerShape(Spacing.md),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                color = Color.Gray
            ) {
                Icon(
                    Icons.Default.Person, contentDescription = null,
                    modifier = Modifier.padding(Spacing.md),
                    tint = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                user.name.orEmpty(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                user.bornDate.orEmpty().dateToLongDate(),
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun BeATrainerBanner(show: Boolean) {
    if (!show) return

    Card(
        shape = RoundedCornerShape(Spacing.md),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(Spacing.md)) {
            Text(
                "Quero ser um treinador",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Icon(Icons.Default.FitnessCenter, contentDescription = null)
        }
    }
}

@Composable
private fun ProfileOptions() {
    Card(
        shape = RoundedCornerShape(Spacing.md),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
        ) {
            ProfileOptionButton("Gerenciar planos", Icons.Default.WorkspacePremium) { }
            HorizontalDivider()
            ProfileOptionButton("Editar perfil", Icons.Default.Edit) { }
            HorizontalDivider()
            ProfileOptionButton("Configurações", Icons.Default.Settings) { }
        }
    }
}

@Composable
private fun Logout(
    logOut: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(Spacing.md),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
        ) {
            ProfileOptionButton("Sair do app", Icons.AutoMirrored.Filled.ExitToApp, action = logOut)
        }
    }
}

@Composable
private fun ProfileOptionButton(name: String, icon: ImageVector, action: () -> Unit) {
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

@Composable
fun FindATrainer(show: Boolean) {
    if (!show) return


}

@Composable
private fun TrainerCard(
    masterSummary: MasterSummary?,
    modifier: Modifier,
    onManageMasterClick: () -> Unit,
) {
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

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                "Gerenciar",
                modifier = Modifier.fillMaxWidth(),
                onClick = onManageMasterClick
            )
        }
    }
}

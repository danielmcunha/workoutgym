package com.southapps.ui.screen.onboarding.selectYourJourney

import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.southapps.core.designSystem.icons.SvgIcons
import com.southapps.core.designSystem.tokens.Spacing
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import workoutapp.composeapp.generated.resources.Res

@Composable
fun OnboardingSelectYourJourneyScreen(
    viewModel: OnboardingSelectYourJourneyViewModel = koinViewModel(),
) = Column(
    modifier = Modifier.fillMaxSize()
        .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Spacing.xl))

        Text(
            "Sua jornada",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(Modifier.height(Spacing.md))

        Text(
            "Como você deseja iniciar?",
            style = MaterialTheme.typography.titleMedium
        )
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectorCard(
            SvgIcons.Dumbell,
            "Sou um treinador",
            "Crie, gerencie a acompanhe os treinos dos seus alunos",
            onClick = viewModel::startMasterOnBoarding
        )

        Spacer(Modifier.height(Spacing.md))

        SelectorCard(
            SvgIcons.WaterBottle,
            "Apenas treinar",
            "Gerencie e acompanhe os seus próprios treinos",
            onClick = viewModel::startChickenOnboarding
        )
    }

    Text(
        "Caso você opte por apenas treinar agora, no futuro você pode se tornar um treinador através do seu perfil",
        style = MaterialTheme.typography.bodyMedium
    )

}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SelectorCard(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .alpha(.8f)
            .clickable {
                onClick()
            },
    ) {
        Row(
            modifier = Modifier
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            AsyncImage(
                model = Res.getUri(icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

package com.southapps.ui.screen.onboarding.welcome

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.icons.SvgIcons
import com.southapps.core.designSystem.tokens.Spacing
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import workoutapp.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OnboardingWelcomeScreen(
    viewModel: OnboardingWelcomeViewModel = koinViewModel(),
) = Column(
    modifier = Modifier.fillMaxSize()
        .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(Spacing.xl))

        Text(
            "Bem-vindo",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(Modifier.height(Spacing.md))

        Text(
            "Gerencie seus treinos, bata suas metas ou, se quiser, vire um treinador e ajude outros nessa jornada!",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(Spacing.xxl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = Res.getUri(SvgIcons.WaterBottle),
                contentDescription = null,
                modifier = Modifier.size(54.dp),
            )

            AsyncImage(
                model = Res.getUri(SvgIcons.Dumbell),
                contentDescription = null,
                modifier = Modifier.size(76.dp),
            )

            AsyncImage(
                model = Res.getUri(SvgIcons.CardioLoad),
                contentDescription = null,
                modifier = Modifier.size(54.dp),
            )
        }
    }

    CustomButton(
        text = "Começar",
        onClick = { viewModel.start() },
        modifier = Modifier.fillMaxWidth()
    )
}

package com.southapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.southapps.core.designSystem.tokens.AppShapes
import com.southapps.core.designSystem.tokens.AppTypography
import com.southapps.core.designSystem.tokens.ColorScheme
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.data.session.SessionStatus
import com.southapps.ui.scaffold.AppScaffold
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import workoutapp.composeapp.generated.resources.Res
import workoutapp.composeapp.generated.resources.logo

@Composable
@Preview
fun App(viewModel: AppEntryViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    KoinContext {
        MaterialTheme(
            colorScheme = ColorScheme,
            typography = AppTypography,
            shapes = AppShapes
        ) {
            val navController = rememberNavController()

            when (uiState.sessionStatus) {
                SessionStatus.Initializing -> {
                    LoadingScreen()
                }

                is SessionStatus.Authenticated, is SessionStatus.Unauthenticated -> {
                    AppScaffold(navController = navController)
                }

                is SessionStatus.Error -> {
//                    ErrorScreen(
//                        errorDetails = status.errorDetails,
//                        onRetry = { viewModel.retry() }
//                    )
                }

                else -> {
                    LoadingScreen()
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(Spacing.xl)
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )
        }
    }
}
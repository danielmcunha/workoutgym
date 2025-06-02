package com.southapps.ui.screen.master.chicken.list

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.ui.screen.master.components.ChickenCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ChickenListScreen(
    viewModel: ChickenListViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedContentScope
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        updateHeader(HeaderContent(title = "Alunos", showBackButton = true))
    }

    LazyColumn(
        Modifier.padding(horizontal = Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        items(uiState.chickens) { chicken ->
            val sharedContentState = rememberSharedContentState(key = chicken.userReference)

            ChickenCard(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = sharedContentState,
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                chicken = chicken,
                expanded = false,
                onClick = {
                    viewModel.onDetail(chicken.userId, chicken.userReference)
                }
            )
        }
    }
}
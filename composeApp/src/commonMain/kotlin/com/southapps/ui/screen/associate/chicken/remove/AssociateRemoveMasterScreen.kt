package com.southapps.ui.screen.associate.chicken.remove

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AssociateRemoveMasterScreen(viewModel: AssociateRemoveMasterViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    LaunchedEffect(Unit) {
        register(viewModel.uiAction)
        updateHeader(
            HeaderContent(
                title = "Remover treinador",
                showBackButton = true
            )
        )
    }
}
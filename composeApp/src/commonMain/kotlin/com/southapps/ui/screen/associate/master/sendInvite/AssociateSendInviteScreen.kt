package com.southapps.ui.screen.associate.master.sendInvite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.HeaderContent
import com.southapps.core.designSystem.LocalHeaderContent
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.components.FormTextField
import com.southapps.core.designSystem.tokens.Spacing
import org.koin.compose.koinInject

@Composable
fun AssociateSendInviteScreen(viewModel: AssociateSendInviteViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current
    val updateHeader = LocalHeaderContent.current

    var email by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose {

        }
    }

    LaunchedEffect(Unit) {
        updateHeader(
            HeaderContent(
                title = "Convidar aluno",
                showBackButton = true
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(Spacing.md)
    ) {
        Text("Informe o e-mail do aluno que você deseja convidar")

        FormTextField(
            formField = uiState.email,
            onValueChange = viewModel::updateEmail,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            "Enviar convite",
            onClick = { viewModel.onAssociateClick() },
            modifier = Modifier.fillMaxWidth(),
            loading = uiState.isLoading
        )
    }
}
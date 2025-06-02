package com.southapps.ui.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.components.AppLogo
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.icons.BarbellIcon
import com.southapps.core.designSystem.tokens.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.lg)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo()

        Icon(
            imageVector = BarbellIcon,
            contentDescription = "",
            modifier = Modifier.scale(2.8f, 1.6f)
        )

        OutlinedTextField(
            value = uiState?.email.orEmpty(),
            onValueChange = viewModel::updateEmail,
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        OutlinedTextField(
            value = uiState?.password.orEmpty(),
            onValueChange = viewModel::updatePassword,
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        CustomButton(
            text = "Entrar",
            loading = uiState.loading,
            onClick = viewModel::signIn,
            modifier = Modifier.fillMaxWidth()
        )

        if(uiState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(6.dp))
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        Text(
            text = "Esqueci minha senha",
            modifier = Modifier
                .clickable {
                    viewModel.resetPassword()
                }
                .padding(Spacing.xs)
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        Text(
            text = "Ainda não tem conta? Cadastre-se!",
            modifier = Modifier
                .clickable {
                    viewModel.signUp()
                }
                .padding(Spacing.xs))

        OutlinedTextField(
            value = uiState?.password.orEmpty(),
            onValueChange = viewModel::updatePassword,
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

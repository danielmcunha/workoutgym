package com.southapps.ui.screen.register

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.components.FormTextField
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.core.resources.AppStrings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = AppStrings.CREATE_ACCOUNT,
            style = MaterialTheme.typography.displaySmall,
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        FormTextField(
            formField = uiState.fullName,
            onValueChange = viewModel::updateFullName,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        FormTextField(
            formField = uiState.email,
            onValueChange = viewModel::updateEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        FormTextField(
            formField = uiState.phone,
            onValueChange = viewModel::updatePhone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        FormTextField(
            formField = uiState.password,
            onValueChange = viewModel::updatePassword,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        FormTextField(
            formField = uiState.passwordConfirmation,
            onValueChange = viewModel::updatePasswordConfirmation,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.lg))

        CustomButton(
            text = AppStrings.REGISTER,
            onClick = {
                viewModel.signUp()
            },
            loading = uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (uiState.error?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        Text(
            text = AppStrings.ALREADY_HAVE_AN_ACCOUNT,
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    viewModel.signIn()
                })
    }

}
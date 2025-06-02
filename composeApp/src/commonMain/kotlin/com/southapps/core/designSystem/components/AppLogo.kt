package com.southapps.core.designSystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GYM",
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = "WORKOUT",
            style = MaterialTheme.typography.displaySmall,
        )
    }
}
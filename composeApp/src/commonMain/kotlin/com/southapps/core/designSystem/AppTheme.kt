package com.southapps.designSystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.southapps.core.designSystem.tokens.AppShapes
import com.southapps.core.designSystem.tokens.AppTypography
import com.southapps.core.designSystem.tokens.ColorScheme

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

package com.southapps.core.designSystem.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(Spacing.sm),
    small = RoundedCornerShape(12.0.dp),
    medium = RoundedCornerShape(Spacing.md),
    large = RoundedCornerShape(20.0.dp),
    extraLarge = RoundedCornerShape(Spacing.lg)
)
package com.southapps.core.designSystem

import androidx.compose.ui.graphics.vector.ImageVector

data class HeaderIcon(
    val icon: ImageVector,
    val action: () -> Unit
)

data class HeaderContent(
    val title: String,
    val showBackButton: Boolean = false,
    val subtitle: String? = null,
    val icons: List<HeaderIcon>?= null
)
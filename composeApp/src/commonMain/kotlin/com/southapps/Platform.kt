package com.southapps

import androidx.compose.runtime.Composable
import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect val preferenceModule: Module

expect fun getPlatform(): Platform

@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

@Composable
expect fun IsKeyboardVisible() : Boolean
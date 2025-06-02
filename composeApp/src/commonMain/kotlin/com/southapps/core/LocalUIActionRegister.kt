package com.southapps.core

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.SharedFlow

val LocalUIActionRegister = staticCompositionLocalOf<(SharedFlow<UIAction>) -> Unit> {
    error("UIActionRegister not provided")
}
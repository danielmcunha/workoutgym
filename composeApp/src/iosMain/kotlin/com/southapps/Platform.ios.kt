package com.southapps

import androidx.compose.runtime.Composable
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // no-op no iOS
}

actual val preferenceModule = module {
    single { createDataStore() }
}

@Composable
actual fun IsKeyboardVisible() : Boolean {
    return false
}
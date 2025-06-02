package com.southapps

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}

actual val preferenceModule = module {
    single { createDataStore(androidContext()) }
}

@Composable
actual fun IsKeyboardVisible() : Boolean {
    return false
}
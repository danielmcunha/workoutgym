package com.southapps.core.designSystem

import androidx.compose.runtime.staticCompositionLocalOf

val LocalHeaderContent = staticCompositionLocalOf<( (HeaderContent?) -> Unit )> {
    { _ -> }
}
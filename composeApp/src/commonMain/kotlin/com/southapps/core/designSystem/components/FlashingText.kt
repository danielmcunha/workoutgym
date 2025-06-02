package com.southapps.core.designSystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun FlashingText(
    text: String,
    style: TextStyle,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
) {
    var previousValue by remember { mutableStateOf(text) }
    val changed = text != previousValue

    val animatedColor by animateColorAsState(
        targetValue = if (changed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(durationMillis = 300),
        label = "flashColor"
    )

    LaunchedEffect(text) {
        if (changed) {
            delay(500)
            previousValue = text
        }
    }

    Text(
        text = text,
        color = animatedColor,
        style = style,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

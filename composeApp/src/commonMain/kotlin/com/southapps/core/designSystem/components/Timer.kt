package com.southapps.core.designSystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.southapps.core.extensions.formatTime
import kotlinx.coroutines.delay

@Composable
fun Timer(
    startTimer: Long,
    modifier: Modifier = Modifier,
) {
    var seconds by remember { mutableStateOf(startTimer) }

    LaunchedEffect(true) {
        while (true) {
            delay(1000L)
            seconds++
        }
    }

    Text(
        text = seconds.formatTime(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}
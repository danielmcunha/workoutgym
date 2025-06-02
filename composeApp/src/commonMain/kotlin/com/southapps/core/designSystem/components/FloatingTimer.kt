package com.southapps.core.designSystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.southapps.core.designSystem.icons.SvgIcons
import com.southapps.core.designSystem.tokens.Spacing
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import workoutapp.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FloatingTimer() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var expanded by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0) }

    val timeOptions = listOf(30, 45, 60, 90, 120, 150)

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0) isRunning = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top control: Timer and start/pause button
                if (expanded) {
                    Column(
                        modifier = Modifier.fillMaxWidth(.7f)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (timeLeft > 0) "$timeLeft s" else "Ready",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            IconButton(onClick = {
                                if (timeLeft > 0) {
                                    isRunning = !isRunning
                                }
                            }) {
                                Icon(
                                    imageVector = if (isRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = {
                                timeLeft = 0
                                isRunning = false
                                expanded = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {

                        }
                    }

                } else {
                    // Floating Button Initial State
                    FloatingActionButton(
                        onClick = { expanded = true },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        AsyncImage(
                            model = Res.getUri(SvgIcons.Timer),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
package com.southapps.core.designSystem.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.southapps.core.designSystem.tokens.Spacing
import kotlinx.coroutines.launch

data class BarSeriesData(
    val value: Float,
    val label: String,
)

data class BarSeries(
    val values: List<BarSeriesData>,
    val colorSelected: Color,
    val color: Color,
)

data class LineSeries(
    val values: List<Float>,
    val color: Color,
)

data class BarLineData(
    val barSeries: BarSeries,
    val lineSeries: LineSeries,
)

@Composable
fun BarLineChart(
    data: BarLineData,
    modifier: Modifier = Modifier,
    animationDuration: Int = 1000,
    barWidthDp: Dp = 8.dp,
    barSpacingDp: Dp = 48.dp,
    onBarSelected: ((index: Int) -> Unit)? = null,
) {
    val animatedProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall

    val totalBarWidth = barWidthDp + barSpacingDp
    val canvasWidth = (totalBarWidth * data.barSeries.values.size)

    var selectedIndex by remember { mutableStateOf(data.barSeries.values.lastIndex) }
    val textSpacing = 40.dp

    LaunchedEffect(data) {
        coroutineScope.launch {
            scrollState.scrollTo(scrollState.maxValue)

            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDuration)
            )
        }
    }

    Column {
        Row(
            modifier = modifier
                .horizontalScroll(scrollState)
                .graphicsLayer { clip = true }
        ) {
            Canvas(
                modifier = Modifier.width(canvasWidth)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val barWidthPx = barWidthDp.toPx()
                            val spacingPx = barSpacingDp.toPx()
                            val total = barWidthPx + spacingPx
                            val index = (offset.x / total).toInt()

                            if (index in data.barSeries.values.indices) {
                                selectedIndex = index
                                onBarSelected?.invoke(index)
                            }
                        }
                    }) {
                if (data.barSeries.values.isEmpty()) return@Canvas

                val barsMaxData = data.barSeries.values.maxOfOrNull { it.value } ?: 0f
                val lineMaxData = data.lineSeries.values.max()
                val lineFactory = barsMaxData / lineMaxData

                val points = mutableListOf<Offset>()

                val barWidthPx = barWidthDp.toPx()
                val halfBarWidthPx = barWidthPx / 2f
                val spacingPx = barSpacingDp.toPx()

                data.barSeries.values.forEachIndexed { index, barSerie ->
                    val xCenter = (barWidthPx + spacingPx) * index + barWidthPx / 2
                    val barHeight =
                        (barSerie.value / barsMaxData) * (size.height - textSpacing.toPx()) * animatedProgress.value
                    val yBar = size.height - barHeight - textSpacing.toPx()

                    val yLine = data.lineSeries.values.getOrNull(index)?.let {
                        val lineHeight =
                            (it / barsMaxData) * (size.height - textSpacing.toPx()) * (animatedProgress.value)

                        size.height - (lineHeight * lineFactory) - textSpacing.toPx()
                    } ?: textSpacing.toPx()

                    points.add(Offset(xCenter, yLine))

                    drawBar(
                        xCenter - barWidthPx / 2,
                        yBar,
                        data.barSeries.colorSelected,
                        data.barSeries.color,
                        Size(barWidthPx, barHeight),
                        halfBarWidthPx,
                        index == selectedIndex
                    )

                    drawLabel(barSerie.label, xCenter, textMeasurer, textStyle)

                }

                drawLine(linePath(points), data.lineSeries.color)
            }
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        DrawSummary(data, selectedIndex)
    }
}

@Composable
private fun DrawSummary(data: BarLineData, selectedIndex: Int) {
    if(data.barSeries.values.isEmpty()) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.padding(horizontal = Spacing.md)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                .padding(horizontal = Spacing.md, vertical = Spacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(8.dp)
                    .background(data.barSeries.colorSelected, RoundedCornerShape(4.dp))
            )

            Text("${data.barSeries.values[selectedIndex].value.toInt()} kg")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                .padding(horizontal = Spacing.md, vertical = Spacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(4.dp)
                    .background(data.lineSeries.color, RoundedCornerShape(4.dp))
            )

            Text("${data.lineSeries.values[selectedIndex].toInt()} reps")
        }
    }
}

private fun DrawScope.drawBar(
    x: Float,
    y: Float,
    colorSelected: Color,
    color: Color,
    size: Size,
    radius: Float,
    selected: Boolean,
) {
    drawRoundRect(
        color = if (selected) colorSelected else color,
        topLeft = Offset(
            x,
            y
        ),
        size = size,
        cornerRadius = CornerRadius(radius)
    )
}

private fun DrawScope.drawLabel(
    label: String,
    x: Float,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
) {
    val measuredText = textMeasurer.measure(
        text = AnnotatedString(label),
        style = textStyle,
    )
    drawText(
        textLayoutResult = measuredText,
        color = Color.Gray,
        topLeft = Offset(
            x = x - measuredText.size.width / 2f,
            y = size.height - measuredText.size.height.toFloat()
        )
    )
}

private fun linePath(points: MutableList<Offset>): Path {
    val path = Path()
    if (points.size >= 2) {
        path.moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val mid = Offset((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
            path.quadraticTo(prev.x, prev.y, mid.x, mid.y)
        }
        path.lineTo(points.last().x, points.last().y)
    }

    return path
}

private fun DrawScope.drawLine(path: Path, color: Color) {
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}
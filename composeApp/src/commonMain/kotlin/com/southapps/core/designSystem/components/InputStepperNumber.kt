package com.southapps.core.designSystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.southapps.core.designSystem.tokens.Spacing
import kotlin.math.pow
import kotlin.math.roundToInt

private fun Float.paddingDecimals(decimals: Int = 2): String {
    val string = this.toString()
    val parts = string.split(".")
    val decimalPart = parts.getOrNull(1) ?: ""
    val paddedDecimal = decimalPart.padEnd(decimals, '0').take(decimals)
    return "${parts[0]}.$paddedDecimal"
}

private fun Float.roundToDecimals(decimals: Int = 2): Float {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor.toFloat()
}

@Composable
fun InputStepperNumber(
    title: String,
    value: String,
    suffix: String,
    decimals: Int,
    step: Float,
    onChange: (Float) -> Unit,
) = Column(
    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
) {
    var numericValue by remember { mutableStateOf(value.toFloat()) }

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )

    Row(
        modifier = Modifier
            .border(
                width = 1.dp, shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.onBackground
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            numericValue -= step
            onChange(numericValue.roundToDecimals())
        }) {
            Text(
                text = "-",
                style = MaterialTheme.typography.displaySmall
            )
        }

        Text(
            text = "${numericValue.paddingDecimals(decimals)} $suffix",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(100.dp)
        )

        TextButton(onClick = {
            numericValue += step
            onChange(numericValue.roundToDecimals())
        }) {
            Text(
                text = "+",
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}
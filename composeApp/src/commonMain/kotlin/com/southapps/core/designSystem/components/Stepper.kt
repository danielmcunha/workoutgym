package com.southapps.core.designSystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.southapps.core.designSystem.tokens.Spacing

@Composable
fun Stepper(
    currentIndex: Int, itemsCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        repeat(itemsCount) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = if (it <= currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = .7f
                ),
                thickness = 2.dp
            )
        }
    }
}

@Composable
fun StepperField(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    step: Int = 1,
    min: Int = 0,
    max: Int = 99,
) {
    Row(
        modifier = modifier
            .heightIn(min = 40.dp)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        if(label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "–",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(36.dp)
                    .clickable(enabled = value > min) {
                        onValueChange(value - step)
                    }
                    .padding(vertical = Spacing.sm),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(36.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = "+",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(36.dp)
                    .clickable(enabled = value < max) {
                        onValueChange(value + step)
                    }
                    .padding(vertical = Spacing.sm),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
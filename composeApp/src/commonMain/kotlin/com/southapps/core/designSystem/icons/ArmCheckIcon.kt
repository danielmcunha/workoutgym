package com.southapps.core.designSystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArmCheckIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ArmWithCheckIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color(0xFF4CAF50)),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(4f, 14f)
            curveTo(4f, 10.7f, 6.7f, 8f, 10f, 8f)
            lineTo(11f, 8f)
            curveTo(12.1f, 8f, 13f, 7.1f, 13f, 6f)
            curveTo(13f, 4.9f, 12.1f, 4f, 11f, 4f)
            lineTo(9f, 4f)
            curveTo(5.7f, 4f, 3f, 6.7f, 3f, 10f)
            lineTo(3f, 14f)
            curveTo(3f, 16.2f, 4.8f, 18f, 7f, 18f)
            lineTo(11f, 18f)
            curveTo(13.2f, 18f, 15f, 16.2f, 15f, 14f)
            lineTo(15f, 12f)
            lineTo(13f, 12f)
            lineTo(13f, 14f)
            curveTo(13f, 15.1f, 12.1f, 16f, 11f, 16f)
            lineTo(7f, 16f)
            curveTo(5.9f, 16f, 5f, 15.1f, 5f, 14f)
            close()
        }

        path(
            fill = SolidColor(Color.White),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(10f, 13.5f)
            lineTo(7.5f, 11f)
            lineTo(6.1f, 12.4f)
            lineTo(10f, 16.3f)
            lineTo(17f, 9.3f)
            lineTo(15.6f, 7.9f)
            close()
        }
    }.build()

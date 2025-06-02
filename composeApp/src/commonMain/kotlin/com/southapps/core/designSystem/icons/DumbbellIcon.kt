package com.southapps.core.designSystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var dumbbellIcon: ImageVector? = null

val DumbbellIcon: ImageVector
    get() {
        if (dumbbellIcon != null) return dumbbellIcon as ImageVector
        dumbbellIcon = ImageVector.Builder(
            name = "DumbbellIcon", defaultWidth = 100.dp, defaultHeight = 100.dp,
            viewportWidth = 512f, viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                stroke = null,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(132f, 200f)
                lineTo(132f, 312f)
                lineTo(152f, 312f)
                lineTo(152f, 200f)
                close()

                moveTo(160f, 180f)
                lineTo(160f, 332f)
                lineTo(180f, 332f)
                lineTo(180f, 180f)
                close()

                moveTo(188f, 160f)
                lineTo(188f, 352f)
                lineTo(204f, 352f)
                lineTo(204f, 160f)
                close()

                moveTo(204f, 240f)
                lineTo(308f, 240f)
                lineTo(308f, 272f)
                lineTo(204f, 272f)
                close()

                moveTo(308f, 160f)
                lineTo(308f, 352f)
                lineTo(324f, 352f)
                lineTo(324f, 160f)
                close()

                moveTo(332f, 180f)
                lineTo(332f, 332f)
                lineTo(352f, 332f)
                lineTo(352f, 180f)
                close()

                moveTo(360f, 200f)
                lineTo(360f, 312f)
                lineTo(380f, 312f)
                lineTo(380f, 200f)
                close()
            }
        }.build()
        return dumbbellIcon as ImageVector
    }


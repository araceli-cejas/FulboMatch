package com.matchball.fulbomatch.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.matchball.fulbomatch.ui.theme.GreenPrimary

@Composable
fun MoonIconButton(
    isDarkMode: Boolean = false,
    iconColor: Color = GreenPrimary,
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
        if (isDarkMode) {
            SunIcon(color = iconColor)
        } else {
            MoonIcon(color = iconColor)
        }
    }
}

@Composable
private fun MoonIcon(
    color: Color
) {
    Canvas(
        modifier = Modifier.size(24.dp)
    ) {
        val outerRadius = size.minDimension * 0.42f
        val centerX = size.width * 0.45f
        val centerY = size.height * 0.5f

        val innerRadius = outerRadius * 0.82f
        val innerCenterX = centerX + outerRadius * 0.42f
        val innerCenterY = centerY - outerRadius * 0.10f

        val outerPath = Path().apply {
            addOval(
                Rect(
                    center = Offset(centerX, centerY),
                    radius = outerRadius
                )
            )
        }

        val innerPath = Path().apply {
            addOval(
                Rect(
                    center = Offset(innerCenterX, innerCenterY),
                    radius = innerRadius
                )
            )
        }

        val crescentPath = Path.combine(
            operation = PathOperation.Difference,
            path1 = outerPath,
            path2 = innerPath
        )

        drawPath(
            path = crescentPath,
            color = color,
            style = Fill
        )
    }
}

@Composable
private fun SunIcon(
    color: Color
) {
    Canvas(
        modifier = Modifier.size(24.dp)
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.18f
        val rayStart = size.minDimension * 0.32f
        val rayEnd = size.minDimension * 0.44f
        val strokeWidth = 2.2.dp.toPx()

        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        val angles = listOf(0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f)

        angles.forEach { angle ->
            val radians = Math.toRadians(angle.toDouble())

            val start = Offset(
                x = center.x + kotlin.math.cos(radians).toFloat() * rayStart,
                y = center.y + kotlin.math.sin(radians).toFloat() * rayStart
            )

            val end = Offset(
                x = center.x + kotlin.math.cos(radians).toFloat() * rayEnd,
                y = center.y + kotlin.math.sin(radians).toFloat() * rayEnd
            )

            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}
package com.matchball.fulbomatch.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.matchball.fulbomatch.ui.theme.GreenPrimary

@Composable
fun MoonIconButton(
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
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
                    androidx.compose.ui.geometry.Rect(
                        center = Offset(centerX, centerY),
                        radius = outerRadius
                    )
                )
            }

            val innerPath = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        center = Offset(innerCenterX, innerCenterY),
                        radius = innerRadius
                    )
                )
            }

            val crescentPath = Path.combine(
                PathOperation.Difference,
                outerPath,
                innerPath
            )

            drawPath(
                path = crescentPath,
                color = GreenPrimary,
                style = Fill
            )
        }
    }
}
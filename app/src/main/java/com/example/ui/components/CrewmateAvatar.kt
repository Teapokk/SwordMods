package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CrewmateAvatar(
    modifier: Modifier = Modifier,
    bodyColor: Color = Color(0xFF00E5FF),
    size: Dp = 64.dp,
    showGlow: Boolean = true
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val strokeColor = Color(0xFF080D1A)
        val strokeWidth = w * 0.05f

        // Shadow under crewmate
        drawOval(
            color = Color(0x33000000),
            topLeft = Offset(w * 0.15f, h * 0.85f),
            size = Size(w * 0.7f, h * 0.15f)
        )

        // Backpack
        drawRoundRect(
            color = bodyColor,
            topLeft = Offset(w * 0.12f, h * 0.32f),
            size = Size(w * 0.22f, h * 0.42f),
            cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
        )
        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(w * 0.12f, h * 0.32f),
            size = Size(w * 0.22f, h * 0.42f),
            cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
            style = Stroke(width = strokeWidth)
        )

        // Main Body
        val bodyPath = Path().apply {
            moveTo(w * 0.32f, h * 0.80f) // Left foot bottom
            lineTo(w * 0.32f, h * 0.85f)
            lineTo(w * 0.46f, h * 0.85f)
            lineTo(w * 0.46f, h * 0.76f) // Crotch
            lineTo(w * 0.54f, h * 0.76f)
            lineTo(w * 0.54f, h * 0.85f)
            lineTo(w * 0.68f, h * 0.85f) // Right foot
            lineTo(w * 0.68f, h * 0.40f)
            // Head arch
            cubicTo(
                w * 0.68f, h * 0.15f,
                w * 0.32f, h * 0.15f,
                w * 0.32f, h * 0.40f
            )
            close()
        }

        drawPath(path = bodyPath, color = bodyColor)
        drawPath(path = bodyPath, color = strokeColor, style = Stroke(width = strokeWidth))

        // Visor (Glow)
        val visorRect = Size(w * 0.36f, h * 0.22f)
        val visorOffset = Offset(w * 0.46f, h * 0.30f)
        val visorCorner = CornerRadius(w * 0.11f, w * 0.11f)

        // Visor border
        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(visorOffset.x - strokeWidth / 2, visorOffset.y - strokeWidth / 2),
            size = Size(visorRect.width + strokeWidth, visorRect.height + strokeWidth),
            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
        )

        // Visor cyan glass
        drawRoundRect(
            color = Color(0xFF6DE8FF),
            topLeft = visorOffset,
            size = visorRect,
            cornerRadius = visorCorner
        )

        // Visor reflection
        drawRoundRect(
            color = Color(0xFFFFFFFF),
            topLeft = Offset(visorOffset.x + w * 0.05f, visorOffset.y + h * 0.03f),
            size = Size(w * 0.16f, h * 0.07f),
            cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )
    }
}

package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

internal class PauseIcon : AbstractPainter() {
    override fun DrawScope.onDraw() {
        val left = size.width * .2f
        val right = size.width * .8f

        val strokeWidth = size.width * .2f

        scale(.8f) {
            drawLine(
                color = defaultColor,
                strokeWidth = strokeWidth,
                start = Offset(left, 0f),
                end = Offset(left, size.height),
                cap = StrokeCap.Round
            )
            drawLine(
                color = defaultColor,
                strokeWidth = strokeWidth,
                start = Offset(right, 0f),
                end = Offset(right, size.height),
                cap = StrokeCap.Round
            )
        }
    }
}

@ResourcePreview
@Composable
private fun PauseIconPreview() {
    Icon(
        painter = Icons.pauseIcon,
        contentDescription = null,
        tint = Color.Blue,
        modifier = Modifier.fillMaxSize()
    )
}
package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

internal class PlayIcon : AbstractPainter() {
    override fun DrawScope.onDraw() {
        val path = Path().apply {
            val cornerRadius = size.width * .1f
            moveTo(0f, cornerRadius)
            lineTo(0f, size.height - cornerRadius)
            quadraticTo(0f, size.height, cornerRadius, size.height)
            lineTo(size.width - cornerRadius, center.y + cornerRadius)
            quadraticTo(
                size.width + (size.width * .1f), center.y,
                size.width - cornerRadius, center.y - cornerRadius
            )
            lineTo(cornerRadius, 0f)
            quadraticTo(0f, 0f, 0f, cornerRadius)
        }

        scale(.9f) {
            drawPath(
                path = path,
                color = defaultColor
            )
        }
    }
}

@ResourcePreview
@Composable
private fun PlayIconPreview() {
    Icon(
        painter = Icons.playIcon,
        contentDescription = null,
        tint = Color.Blue,
        modifier = Modifier.fillMaxSize()
    )
}
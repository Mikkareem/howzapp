package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

internal class StopIcon : AbstractPainter() {
    override fun DrawScope.onDraw() {
        val cornerRadius = size.width * .1f

        scale(.9f) {
            drawRoundRect(
                color = defaultColor,
                cornerRadius = CornerRadius(cornerRadius)
            )
        }
    }
}


@ResourcePreview
@Composable
private fun StopIconPreview() {
    Icon(
        painter = Icons.stopIcon,
        contentDescription = null,
        tint = Color.Blue,
        modifier = Modifier.fillMaxSize()
    )
}
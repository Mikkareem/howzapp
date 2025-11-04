package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate

internal class SendIcon: AbstractPainter() {
    override fun DrawScope.onDraw() {
        val extremeLeft = 0f
        val extremeRight = size.width
        val innerLeft = size.width * .25f

        val extremeTop = 0f
        val extremeBottom = size.height

        val path = Path().apply {
            moveTo(innerLeft, center.y)
            lineTo(extremeLeft, extremeTop)
            lineTo(extremeRight, center.y)
            lineTo(extremeLeft, extremeBottom)
            close()
        }

        translate(
            left = size.width * .15f,
            top = -size.height * .15f
        ) {
            rotate(-45f) {
                drawPath(
                    path = path,
                    color = defaultColor,
                )
            }
        }
    }
}

@ResourcePreview
@Composable
private fun SendIconPreview() {
    Icon(
        painter = Icons.sendIcon,
        contentDescription = null,
        tint = Color.Blue,
        modifier = Modifier.fillMaxSize()
    )
}
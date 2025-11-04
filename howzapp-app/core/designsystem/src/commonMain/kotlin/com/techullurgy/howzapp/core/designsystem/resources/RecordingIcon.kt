package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

internal class RecordingIcon: AbstractPainter() {
    override fun DrawScope.onDraw() {
        val ovalRect1 = Rect(
            left = size.width * .3f,
            top = size.height * .1f,
            right = size.width * .7f,
            bottom = size.height * .75f
        )

        val ovalRect2 = Rect(
            left = size.width * .15f,
            top = size.height * .35f,
            right = size.width * .85f,
            bottom = size.height * .65f
        )

        val path = Path().apply {
            addArc(ovalRect1, 140f, 260f)
            addArc(ovalRect2, 0f, 180f)
            moveTo(ovalRect2.bottomCenter.x, ovalRect2.bottomCenter.y)
            lineTo(ovalRect2.bottomCenter.x, size.height * .9f)
        }

        drawPath(
            path = path,
            color = defaultColor,
            style = defaultStroke
        )
    }
}

@ResourcePreview
@Composable
private fun RecordingIconPreview() {
    Icon(
        painter = Icons.recordingIcon,
        contentDescription = null,
        tint = Color.Blue,
        modifier = Modifier.fillMaxSize()
    )
}
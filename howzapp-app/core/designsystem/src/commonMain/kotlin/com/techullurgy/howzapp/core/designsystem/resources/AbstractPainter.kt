package com.techullurgy.howzapp.core.designsystem.resources

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter

internal abstract class AbstractPainter: Painter() {
    protected val defaultColor = Color.White

    protected val DrawScope.defaultStroke get() = Stroke(width = size.width * .07f, cap = StrokeCap.Round, join = StrokeJoin.Round)

    override val intrinsicSize: Size = Size.Unspecified
}
package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.LayoutDirection
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import kotlin.time.Instant

@Composable
internal fun MessageViewAnchored(
    message: Message,
    owner: MessageOwner,
    timestamp: Instant,
    arrowDirection: LayoutDirection,
    color: Color,
) {
    Box(
        modifier = Modifier
            .drawBehind {
                val path = Path().apply {
                    val startX = if(arrowDirection == LayoutDirection.Ltr) 0f else size.width
                    val endX = if(arrowDirection == LayoutDirection.Ltr) -40f else size.width + 40f

                    val start = 30f
                    val end = 80f
                    moveTo(startX, start)
                    lineTo(endX,(start + end) * .5f)
                    lineTo(startX, end)
                    close()
                }

                drawPath(path, color)
            }
    ) {
        MessageView(message, owner, timestamp, color)
    }
}
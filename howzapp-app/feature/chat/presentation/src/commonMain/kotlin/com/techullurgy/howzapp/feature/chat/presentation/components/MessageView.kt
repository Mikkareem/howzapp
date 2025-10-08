package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.domain.models.Message

@Composable
internal fun MessageView(
    message: Message
) {
    Box(
        modifier = Modifier
            .drawBehind {
                val path1 = Path().apply {
                    addRoundRect(
                        RoundRect(0f, 0f, size.width, size.height, CornerRadius(20f))
                    )
                }
                drawPath(path1, Color.Blue)
            }
            .padding(12.dp)
    ) {
        when(message) {
            is Message.TextMessage -> {
                Text(message.text, color = Color.White)
            }
            is Message.NonUploadablePendingMessage -> TODO()
            is Message.UploadablePendingMessage -> TODO()
            is Message.AudioMessage -> TODO()
            is Message.DocumentMessage -> TODO()
            is Message.ImageMessage -> TODO()
            is Message.VideoMessage -> TODO()
        }
    }
}
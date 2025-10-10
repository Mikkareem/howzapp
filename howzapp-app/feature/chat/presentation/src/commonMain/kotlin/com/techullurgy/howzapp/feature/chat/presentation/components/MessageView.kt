package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner

@Composable
internal fun MessageView(
    message: Message,
    owner: MessageOwner
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
        Column {
            when (message) {
                is Message.TextMessage -> TextMessageView(message)
                is Message.NonUploadablePendingMessage -> NonUploadablePendingMessageView(message)
                is Message.UploadablePendingMessage -> UploadablePendingMessageView(message)
                is Message.AudioMessage -> AudioMessageView(message)
                is Message.DocumentMessage -> DocumentMessageView(message)
                is Message.ImageMessage -> ImageMessageView(message)
                is Message.VideoMessage -> VideoMessageView(message)
            }

            if (owner is MessageOwner.Me) {
                Text(owner.msgStatus.name)
            }
        }
    }
}

@Composable
private fun TextMessageView(
    message: Message.TextMessage
) {
    Text(message.text, color = Color.White)
}

@Composable
private fun ImageMessageView(
    message: Message.ImageMessage
) {
    Box(
        modifier = Modifier.size(250.dp).background(Color.Green)
    ) {
        Text(message.imageUrl)
    }
}

@Composable
private fun AudioMessageView(
    message: Message.AudioMessage
) {
    Box(
        modifier = Modifier.width(250.dp).height(80.dp).background(Color.Yellow)
    ) {
        Text(message.audioUrl)
    }
}

@Composable
private fun VideoMessageView(
    message: Message.VideoMessage
) {
    Box(
        modifier = Modifier.size(250.dp).background(Color.Yellow)
    ) {
        Text(message.videoUrl)
    }
}

@Composable
private fun DocumentMessageView(
    message: Message.DocumentMessage
) {
    Box(
        modifier = Modifier.width(250.dp).height(80.dp).background(Color.Yellow)
    ) {
        Text(message.documentName)
    }
}

@Composable
private fun NonUploadablePendingMessageView(
    message: Message.NonUploadablePendingMessage
) {
    when (val originalMessage = message.originalMessage) {
        is Message.TextMessage -> TextMessageView(originalMessage)
    }
}

@Composable
private fun UploadablePendingMessageView(
    message: Message.UploadablePendingMessage
) {
    when (val originalMessage = message.originalMessage) {
        is Message.AudioMessage -> AudioMessageView(originalMessage)
        is Message.DocumentMessage -> DocumentMessageView(originalMessage)
        is Message.ImageMessage -> ImageMessageView(originalMessage)
        is Message.VideoMessage -> VideoMessageView(originalMessage)
    }
}
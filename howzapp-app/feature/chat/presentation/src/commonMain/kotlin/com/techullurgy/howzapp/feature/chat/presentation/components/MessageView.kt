package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.presentation.utils.toUIString
import howzapp.feature.chat.presentation.generated.resources.Res
import howzapp.feature.chat.presentation.generated.resources.done_all
import howzapp.feature.chat.presentation.generated.resources.pending
import howzapp.feature.chat.presentation.generated.resources.plus
import howzapp.feature.chat.presentation.generated.resources.sent
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@Composable
internal fun MessageView(
    message: Message,
    owner: MessageOwner,
    timestamp: Instant,
    modifier: Modifier = Modifier,
) {

    var timeString by rememberSaveable {
        mutableStateOf(timestamp.toUIString())
    }

    LaunchedEffect(timestamp) {
        while (timestamp.toUIString().contains("AM").not() || timestamp.toUIString().contains("PM")
                .not()
        ) {
            timeString = timestamp.toUIString()
            delay(1000)
        }
    }

    Box(
        modifier = modifier
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
        CompositionLocalProvider(
            LocalContentColor provides Color.White
        ) {
            Column {
                when (message) {
                    is Message.TextMessage -> TextMessageView(message)
                    is Message.NonUploadablePendingMessage -> NonUploadablePendingMessageView(
                        message
                    )

                    is Message.UploadablePendingMessage -> UploadablePendingMessageView(message)
                    is Message.AudioMessage -> AudioMessageView(message)
                    is Message.DocumentMessage -> DocumentMessageView(message)
                    is Message.ImageMessage -> ImageMessageView(message)
                    is Message.VideoMessage -> VideoMessageView(message)
                }

                Spacer(Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = when (owner) {
                        is MessageOwner.Me -> Arrangement.End
                        is MessageOwner.Other -> Arrangement.Start
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .background(shape = RoundedCornerShape(4.dp), color = Color.Black)
                            .padding(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = timeString, fontSize = 12.sp, lineHeight = 12.sp)
                            if (owner is MessageOwner.Me) {
                                when (owner.msgStatus) {
                                    MessageStatus.PENDING,
                                    MessageStatus.CREATED -> {
                                        Icon(
                                            painter = painterResource(Res.drawable.pending),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    MessageStatus.SENT -> {
                                        Icon(
                                            painter = painterResource(Res.drawable.sent),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    MessageStatus.DELIVERED -> {
                                        Icon(
                                            painter = painterResource(Res.drawable.done_all),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    MessageStatus.READ -> {
                                        Icon(
                                            painter = painterResource(Res.drawable.done_all),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextMessageView(
    message: Message.TextMessage
) {
    Text(message.text)
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (message.status) {
            UploadStatus.Failed -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Red
                        )
                    }
                    Text("Retry", color = Color.Red, fontSize = 14.sp)
                }
            }

            is UploadStatus.Progress -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Green
                        )
                    }
                    Text("Uploading", color = Color.Green, fontSize = 14.sp)
                }
            }

            UploadStatus.Started -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                        )
                    }
                    Text("Pending", fontSize = 14.sp)
                }
            }

            is UploadStatus.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.sent),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Green
                        )
                    }
                    Text("Uploaded", color = Color.Green, fontSize = 14.sp)
                }
            }

            UploadStatus.Triggered -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                        )
                    }
                    Text("Pending", fontSize = 14.sp)
                }
            }

            else -> {}
        }

        when (val originalMessage = message.originalMessage) {
            is Message.AudioMessage -> AudioMessageView(originalMessage)
            is Message.DocumentMessage -> DocumentMessageView(originalMessage)
            is Message.ImageMessage -> ImageMessageView(originalMessage)
            is Message.VideoMessage -> VideoMessageView(originalMessage)
        }
    }
}

@Preview
@Composable
private fun MessageViewPreview(
    @PreviewParameter(MessageViewPreviewParameterProvider::class) data: MessageViewData
) {
    HowzAppTheme {
        MessageView(
            message = data.message,
            owner = data.owner,
            timestamp = data.timestamp,
        )
    }
}

private data class MessageViewData(
    val message: Message,
    val owner: MessageOwner,
    val timestamp: Instant
)

private class MessageViewPreviewParameterProvider : PreviewParameterProvider<MessageViewData> {
    override val values: Sequence<MessageViewData>
        get() = sequenceOf(
            MessageViewData(
                message = Message.TextMessage("Hello People, How are you?"),
                owner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.DELIVERED),
                timestamp = Clock.System.now()
            ),
            MessageViewData(
                message = Message.ImageMessage("Hello People, How are you?"),
                owner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.DELIVERED),
                timestamp = Clock.System.now().minus(34.minutes)
            )
        )
}
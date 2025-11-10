package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.labelXSmall
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.presentation.utils.toUIString
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.done_all
import howzapp.core.presentation.generated.resources.pending
import howzapp.core.presentation.generated.resources.sent
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
    color: Color,
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
                drawPath(path1, color)
            }
//            .innerShadow(RoundedCornerShape(20f)) {
//                spread = 5f
//                radius = 20f
//            }
            .padding(all = 8.dp)
    ) {
        MessageLayout(
            owner = owner
        ) {
            when (message) {
                is PendingMessage -> PendingMessageView(message)
                is OriginalMessage.TextMessage -> TextMessageView(message)
                is OriginalMessage.AudioMessage -> {
                    AudioMessageView(
                        message = message,
                        onPlay = {},
                        onPause = {}
                    )
                }

                is OriginalMessage.DocumentMessage -> DocumentMessageView(message)
                is OriginalMessage.ImageMessage -> ImageMessageView(message)
                is OriginalMessage.VideoMessage -> VideoMessageView(message)
            }

            Box(
                Modifier
                    .background(shape = RoundedCornerShape(4.dp), color = Color.Black)
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelXSmall,
                        color = Color.White
                    )
                    if (owner is MessageOwner.Me) {
                        when (owner.status) {
                            MessageStatus.SenderStatus.PENDING -> {
                                Icon(
                                    painter = painterResource(Res.drawable.pending),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            MessageStatus.SenderStatus.SENT -> {
                                Icon(
                                    painter = painterResource(Res.drawable.sent),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            MessageStatus.SenderStatus.DELIVERED -> {
                                Icon(
                                    painter = painterResource(Res.drawable.done_all),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            MessageStatus.SenderStatus.READ -> {
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

@Composable
private fun MessageLayout(
    modifier: Modifier = Modifier,
    owner: MessageOwner,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        val totalWidth = placeables.maxOf { it.width }
        val totalHeight = placeables.sumOf { it.height }

        layout(totalWidth, totalHeight) {
            val messagePlaceable = placeables.first()
            val timePlaceable = placeables.last()

            messagePlaceable.place(0, 0)

            if (owner is MessageOwner.Me) {
                timePlaceable.place(totalWidth - timePlaceable.width, messagePlaceable.height)
            } else {
                timePlaceable.place(
                    0,
                    messagePlaceable.height
                )
            }
        }
    }
}

@Preview
@Composable
private fun MessageViewPreview(
    @PreviewParameter(MessageViewPreviewParameterProvider::class) data: MessageViewData
) {
    HowzAppTheme {
        Box(
            Modifier.padding(10.dp)
        ) {
            MessageView(
                message = data.message,
                owner = data.owner,
                timestamp = data.timestamp,
                color = Color.Blue
            )
        }
    }
}

private data class MessageViewData(
    val message: OriginalMessage,
    val owner: MessageOwner,
    val timestamp: Instant
)

private class MessageViewPreviewParameterProvider : PreviewParameterProvider<MessageViewData> {
    override val values: Sequence<MessageViewData>
        get() = sequenceOf(
            MessageViewData(
                message = OriginalMessage.TextMessage("Hello People, How are you?"),
                owner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.SenderStatus.DELIVERED),
                timestamp = Clock.System.now()
            ),
            MessageViewData(
                message = OriginalMessage.ImageMessage("Hello People, How are you?"),
                owner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.SenderStatus.DELIVERED),
                timestamp = Clock.System.now().minus(34.minutes)
            ),
            MessageViewData(
                message = OriginalMessage.TextMessage("Hello People, How are you?"),
                owner = MessageOwner.Other(
                    ChatParticipant("", ""),
                    MessageStatus.ReceiverStatus.UNREAD
                ),
                timestamp = Clock.System.now()
            ),
            MessageViewData(
                message = OriginalMessage.ImageMessage("Hello People, How are you?"),
                owner = MessageOwner.Other(
                    ChatParticipant("", ""),
                    MessageStatus.ReceiverStatus.READ
                ),
                timestamp = Clock.System.now().minus(34.minutes)
            )
        )
}
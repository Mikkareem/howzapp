package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.core.designsystem.preview.PreviewUiState
import com.techullurgy.howzapp.core.designsystem.preview.PreviewUiStateParameterProvider
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.extended
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.presentation.utils.toUIString
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ChatPreviewUi
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListUiState
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListViewModel
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.done_all
import howzapp.core.presentation.generated.resources.pending
import howzapp.core.presentation.generated.resources.sent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun ConversationListScreen(
    onConversationClick: (String) -> Unit
) {
    val viewModel = koinViewModel<ConversationListViewModel>()

    val state by viewModel.state.collectAsState()

    ConversationListScreenImpl(state = state, onConversationClick = onConversationClick)
}

@Composable
private fun ConversationListScreenImpl(
    state: ConversationListUiState,
    onConversationClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding(),
    ) {
        when(val state = state) {
            ConversationListUiState.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ConversationListUiState.Data -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.previews) {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .clickable { onConversationClick(it.preview.chatId) }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DisplayPicture(
                                url = it.preview.picture,
                                size = 50.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = it.preview.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.extended.textPrimary
                                )
                                Spacer(Modifier.height(8.dp))
                                AnimatedContent(
                                    targetState = it.subtitleOverride != null
                                ) { isSubtitleOverridden ->
                                    if(isSubtitleOverridden) {
                                        Text(
                                            text = it.subtitleOverride!!,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                    } else {
                                        when (val content = it.preview.lastMessage.content) {
                                            is OriginalMessage -> {
                                                LastMessageView(
                                                    owner = it.preview.lastMessage.owner,
                                                    content = content
                                                )
                                            }

                                            is PendingMessage -> {
                                                LastMessageView(
                                                    owner = it.preview.lastMessage.owner,
                                                    content = content.originalMessage
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(4.dp))
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                AnimatedVisibility(
                                    visible = it.preview.unreadCount > 0
                                ) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            it.preview.unreadCount.toString(),
                                            modifier = Modifier.padding(4.dp),
                                        )
                                    }
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = it.preview.lastMessageTimestamp.toUIString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.extended.textSecondary
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
private fun DisplayPicture(
    url: String?,
    size: Dp
) {
    AsyncImage(
        model = url,
        contentDescription = "Display Picture + $url",
        modifier = Modifier.size(size).clip(CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = object : Painter() {
            override fun DrawScope.onDraw() {
                drawCircle(Color.Magenta, radius = this.size.width * 0.5f)
            }

            override val intrinsicSize: Size
                get() = Size.Unspecified
        },
        fallback = object : Painter() {
            override fun DrawScope.onDraw() {
                drawCircle(Color.Magenta, radius = this.size.width * 0.5f)
            }

            override val intrinsicSize: Size
                get() = Size.Unspecified
        },
    )
}

@Composable
private fun LastMessageView(
    owner: MessageOwner,
    content: OriginalMessage
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.extended.textSecondary,
        LocalTextStyle provides MaterialTheme.typography.bodySmall
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when (owner) {
                is MessageOwner.Me -> {
                    MessageSenderStatusView(owner.status)
                }

                is MessageOwner.Other -> {}
            }
            when (content) {
                is OriginalMessage.AudioMessage -> {
                    Text(
                        text = "Voice Message (00:46)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is OriginalMessage.DocumentMessage -> {
                    Text(
                        text = content.documentName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is OriginalMessage.ImageMessage -> {
                    Text(
                        text = "Image",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is OriginalMessage.TextMessage -> {
                    Text(
                        text = content.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                is OriginalMessage.VideoMessage -> {
                    Text(
                        text = "Video (01:34)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageSenderStatusView(
    status: MessageStatus.SenderStatus
) {
    val resource = when (status) {
        MessageStatus.SenderStatus.PENDING -> Res.drawable.pending
        MessageStatus.SenderStatus.SENT -> Res.drawable.sent
        MessageStatus.SenderStatus.DELIVERED -> Res.drawable.done_all
        MessageStatus.SenderStatus.READ -> Res.drawable.done_all
    }

    Icon(
        modifier = Modifier.size(20.dp),
        painter = painterResource(resource),
        contentDescription = "Message Status",
        tint = if (status == MessageStatus.SenderStatus.READ) Color.Blue else LocalContentColor.current
    )
}

@Preview(showBackground = true)
@Composable
private fun ConversationListScreenPreview(
    @PreviewParameter(ConversationListUiStatePreviewParameterProvider::class) state: PreviewUiState<ConversationListUiState>
) {
    HowzAppTheme(state.isDarkMode) {
        ConversationListScreenImpl(
            state = state.state,
            onConversationClick = {}
        )
    }
}

private class ConversationListUiStatePreviewParameterProvider :
    PreviewUiStateParameterProvider<ConversationListUiState>() {

    override val originalValues = listOf(
        ConversationListUiState.Data(
            previews = listOf(
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "123",
                        title = "Irsath Kareem",
                        picture = "9as89d798sa7d",
                        unreadCount = 0,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0s9",
                            "123",
                            content = OriginalMessage.TextMessage("Good morning"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(ChatParticipant("123", "Irsath Kareem"), MessageStatus.SenderStatus.DELIVERED)
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    )
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "9a8sd",
                        title = "Kareem",
                        picture = "9as89d798sa7d",
                        unreadCount = 0,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0s9",
                            "9a8sd",
                            content = OriginalMessage.TextMessage("Good morning Friend"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.SenderStatus.SENT
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    )
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "89we8",
                        title = "Greece",
                        picture = "9as89d798sa7d",
                        unreadCount = 10,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0iaoisdu",
                            "89we8",
                            content = OriginalMessage.TextMessage("Good morning"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.SenderStatus.DELIVERED
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    ),
                    subtitleOverride = "recording audio..."
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "89we8",
                        title = "Fidal",
                        picture = "9as89d798sa7d",
                        unreadCount = 2,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0iaoisdu",
                            "89we8",
                            content = OriginalMessage.TextMessage("Good morning"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.SenderStatus.DELIVERED
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.minutes)
                    ),
                    subtitleOverride = "typing..."
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "9a8sd",
                        title = "Kareem",
                        picture = "9as89d798sa7d",
                        unreadCount = 0,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0s9",
                            "9a8sd",
                            content = OriginalMessage.TextMessage("Good morning Friend"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.SenderStatus.READ
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    )
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "9a8sd",
                        title = "Kareem",
                        picture = "9as89d798sa7d",
                        unreadCount = 0,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0s9",
                            "9a8sd",
                            content = OriginalMessage.TextMessage("Good morning Friend"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Me(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.SenderStatus.PENDING
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    )
                ),
                ChatPreviewUi(
                    preview = ChatPreview(
                        chatId = "9a8sd",
                        title = "Kareem",
                        picture = "9as89d798sa7d",
                        unreadCount = 1,
                        chatType = ChatType.Direct(
                            ChatParticipant("123", "Irsath Kareem"),
                            ChatParticipant("2324", "Kareem")
                        ),
                        lastMessage = ChatMessage(
                            "9a0s9",
                            "9a8sd",
                            content = OriginalMessage.TextMessage("Good morning Friend"),
                            timestamp = Clock.System.now().minus(3.seconds),
                            owner = MessageOwner.Other(
                                ChatParticipant("123", "Irsath Kareem"),
                                MessageStatus.ReceiverStatus.UNREAD
                            )
                        ),
                        lastMessageTimestamp = Clock.System.now().minus(3.seconds)
                    )
                ),
            )
        )
    )
}
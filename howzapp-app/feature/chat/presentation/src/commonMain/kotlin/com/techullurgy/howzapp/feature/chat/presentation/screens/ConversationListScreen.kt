package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.presentation.utils.toUIString
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ChatPreviewUi
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListUiState
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
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
        modifier = Modifier.fillMaxSize(),
    ) {
        when(val state = state) {
            ConversationListUiState.Loading -> TODO()
            is ConversationListUiState.Data -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.previews) {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .clickable { onConversationClick(it.preview.chatId) }
                                .padding(8.dp)
                        ) {
                            DisplayPicture(
                                url = it.preview.picture,
                                size = 50.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(it.preview.title)

                                AnimatedContent(
                                    targetState = it.subtitleOverride != null
                                ) { isSubtitleOverridden ->
                                    if(isSubtitleOverridden) {
                                        Text(it.subtitleOverride!!)
                                    } else {
                                        // TODO: Last Message Content
                                        Text(it.preview.lastMessage.content.toString())
                                    }
                                }
                            }
                            Spacer(Modifier.width(4.dp))
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(it.preview.unreadCount.toString())
                                Text(it.preview.lastMessageTimestamp.toUIString())
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
                drawCircle(Color.Magenta)
            }

            override val intrinsicSize: Size
                get() = Size.Unspecified
        },
        fallback = object : Painter() {
            override fun DrawScope.onDraw() {
                drawCircle(Color.Magenta)
            }

            override val intrinsicSize: Size
                get() = Size.Unspecified
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ConversationListScreenPreview(
    @PreviewParameter(ConversationListUiStatePreviewParameterProvider::class) state: ConversationListUiState
) {
    HowzAppTheme {
        ConversationListScreenImpl(
            state = state,
            onConversationClick = {}
        )
    }
}

private class ConversationListUiStatePreviewParameterProvider : PreviewParameterProvider<ConversationListUiState> {
    override val values: Sequence<ConversationListUiState> = sequenceOf(
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
                                MessageStatus.SenderStatus.DELIVERED
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
                    )
                )
            )
        )
    )
}
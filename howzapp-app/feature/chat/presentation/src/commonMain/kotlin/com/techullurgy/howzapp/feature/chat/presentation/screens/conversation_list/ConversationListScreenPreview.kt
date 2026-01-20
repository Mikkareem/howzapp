package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.techullurgy.howzapp.core.designsystem.preview.PreviewUiState
import com.techullurgy.howzapp.core.designsystem.preview.PreviewUiStateParameterProvider
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list.viewmodels.ChatPreviewUi
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list.viewmodels.ConversationListUiState
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Preview(showBackground = true)
@Composable
private fun ConversationListScreenPreview(
    @PreviewParameter(ConversationListUiStatePreviewParameterProvider::class) state: PreviewUiState<ConversationListUiState>
) {
    HowzAppTheme(state.isDarkMode) {
        ConversationListScreenRoot(
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
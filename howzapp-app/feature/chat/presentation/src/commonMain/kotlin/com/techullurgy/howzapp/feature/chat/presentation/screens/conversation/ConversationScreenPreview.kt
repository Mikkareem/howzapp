package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageItem
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageFeedItem
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes


@Preview
@Composable
private fun ConversationScreenRootPreview(
    @PreviewParameter(ConversationUiStatePreviewParameterProvider::class) previewState: PreviewProvider
) {
    HowzAppTheme(
        previewState.isDarkMode
    ) {
        val state by remember { mutableStateOf(previewState.state) }
        val inputState by remember { mutableStateOf(previewState.inputState) }

        ConversationScreenRoot(
            state,
            inputState,
            onRecordStarted = {},
            onRecordStopped = {},
            onRecordCancelled = {},
            onMessageSend = {},
            onImageSelected = {},
            onAudioSelected = {},
            onVideoSelected = {},
            onDocumentSelected = { _, _ -> },
            onImageMessageClick = {},
            onVideoMessageClick = {},
            onLocationMessageClick = {_, _ ->}
        )
    }
}

private data class PreviewProvider(
    val isDarkMode: Boolean,
    val state: ConversationUiState,
    val inputState: ConversationInputUiState
)

private class ConversationUiStatePreviewParameterProvider :
    PreviewParameterProvider<PreviewProvider> {
    override val values: Sequence<PreviewProvider>
        get() = sequence {
            buildList {
                add(MessageFeedItem.Badge("Today"))

                addAll(
                    buildList {
                        var mutableMinutes = 4.minutes
                        repeat(10) {
                            val user = listOf(users[0], users[1]).random()
                            val minutes = mutableMinutes + 15.minutes
                            MessageItem(
                                message = messages.random(),
                                messageId = "m1_${lastIndex + 1}",
                                sender = user,
                                isPictureShowable = it % 3 == 0,
                                messageOwner = messageOwners[if (user.userId == users[0].userId) 0 else 4],
                                timestamp = Clock.System.now().minus(minutes)
                            ).run {
                                add(MessageFeedItem.Content(this))
                            }
                            mutableMinutes = minutes
                        }
                    }
                )
            }.run {
                yield(
                    PreviewProvider(
                        false,
                        ConversationUiState(
                            title = "Riyas",
                            subtitle = "Online",
                            profilePicture = "",
                            messageFeedItems = this
                        ),
                        ConversationInputUiState()
                    )
                )

                yield(
                    PreviewProvider(
                        true,
                        ConversationUiState(
                            title = "Riyas",
                            subtitle = "Online",
                            profilePicture = "",
                            messageFeedItems = this
                        ),
                        ConversationInputUiState()
                    )
                )
            }
        }
}

private val users = listOf(
    ChatParticipant("u123", "Irsath", ""),
    ChatParticipant("u456", "Riyas", "")
)

private val messageOwners = listOf(
    MessageOwner.Me(users[0], MessageStatus.SenderStatus.READ),
    MessageOwner.Me(users[0], MessageStatus.SenderStatus.DELIVERED),
    MessageOwner.Me(users[0], MessageStatus.SenderStatus.SENT),
    MessageOwner.Me(users[0], MessageStatus.SenderStatus.PENDING),

    MessageOwner.Other(users[1], MessageStatus.ReceiverStatus.READ),
    MessageOwner.Other(users[1], MessageStatus.ReceiverStatus.UNREAD),
    MessageOwner.Other(users[1], MessageStatus.ReceiverStatus.PENDING),
)

private val messages = listOf(
    OriginalMessage.AudioMessage(""),
    OriginalMessage.TextMessage("How are you?"),
) + listOf(
    "Hey!",
    "How’s it going?",
    "Did you check the new design?",
    "Yes, I liked it!",
    "Let's meet tomorrow.",
    "Sure, what time?",
    "10 AM sounds good.",
    "Perfect!"
).map { OriginalMessage.TextMessage(it) } + listOf(
    24.5, 16.66, 68.0, 92.34, 100.0, 0.0
).map {
    PendingMessage.UploadablePendingMessage(
        status = UploadStatus.Progress(it),
        originalMessage = OriginalMessage.ImageMessage("")
    )
}
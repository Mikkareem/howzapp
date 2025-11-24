package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBadge
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBox
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

@Composable
internal fun ConversationScreenImpl(
    state: ConversationUiState,
    inputState: ConversationInputUiState,
    onRecordStarted: () -> Unit,
    onRecordStopped: () -> Unit,
    onRecordCancelled: () -> Unit,
    onMessageSend: () -> Unit,
    onImageSelected: (String) -> Unit,
    onAudioSelected: (String) -> Unit,
    onVideoSelected: (String) -> Unit,
    onDocumentSelected: (String, String) -> Unit,
    onPlayRecordedAudioInPreview: () -> Unit,
    onPauseRecordedAudioInPreview: () -> Unit,
    onResumeRecordedAudioInPreview: () -> Unit,
    onStopRecordedAudioInPreview: () -> Unit,
    onPlayAudioInPreview: () -> Unit,
    onPauseAudioInPreview: () -> Unit,
    onResumeAudioInPreview: () -> Unit,
    onStopAudioInPreview: () -> Unit,
    onPlayVideoInPreview: () -> Unit,
    onPauseVideoInPreview: () -> Unit,
    onResumeVideoInPreview: () -> Unit,
    onStopVideoInPreview: () -> Unit,
) {
    Scaffold(
        contentColor = LocalContentColor.current,
        modifier = Modifier.fillMaxSize().consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .padding(8.dp, 16.dp)
            ) {
                InfoBox(state)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.padding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
                        .asPaddingValues()
                )
            ) {
                InputBox(
                    inputState = inputState,
                    onRecordStarted = onRecordStarted,
                    onRecordStopped = onRecordStopped,
                    onRecordCancelled = onRecordCancelled,
                    onMessageSend = onMessageSend,
                    onImageSelected = onImageSelected,
                    onAudioSelected = onAudioSelected,
                    onVideoSelected = onVideoSelected,
                    onDocumentSelected = onDocumentSelected,
                    onPlayRecordedAudioPreview = onPlayRecordedAudioInPreview,
                    onPauseRecordedAudioPreview = onPauseRecordedAudioInPreview,
                    onResumeRecordedAudioPreview = onResumeRecordedAudioInPreview,
                    onStopRecordedAudioPreview = onStopRecordedAudioInPreview,
                    onPlayAudioPreview = onPlayAudioInPreview,
                    onPauseAudioPreview = onPauseAudioInPreview,
                    onResumeAudioPreview = onResumeAudioInPreview,
                    onStopAudioPreview = onStopAudioInPreview,
                    onPlayVideoPreview = onPlayVideoInPreview,
                    onPauseVideoPreview = onPauseVideoInPreview,
                    onResumeVideoPreview = onResumeVideoInPreview,
                    onStopVideoPreview = onStopVideoInPreview,
                )
            }
        }
    ) { padding ->

        var overlayBadgeContent by remember { mutableStateOf("") }

        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            launch {
                snapshotFlow { listState.firstVisibleItemIndex }
                    .drop(1)
                    .collectLatest {
                        val firstContentIndex = listState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.contentType == "content" }?.index
                            ?: return@collectLatest

                        val dateString =
                            (state.messageUis[firstContentIndex] as MessageUi.Content).content.timestamp.toLocalDateTime(
                                TimeZone.UTC
                            ).date

                        overlayBadgeContent = dateString.toString()
                    }
            }

            launch {
                snapshotFlow { listState.isScrollInProgress }
                    .drop(1)
                    .transform {
                        if (!it) {
                            delay(1000)
                            emit(Unit)
                        }
                    }
                    .collect {
                        overlayBadgeContent = ""
                    }
            }
        }

        Box(
            modifier = Modifier.padding(padding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.messageUis,
                    key = {
                        when (it) {
                            is MessageUi.Badge -> it.badge
                            is MessageUi.Content -> it.content.messageId
                        }
                    },
                    contentType = {
                        when (it) {
                            is MessageUi.Badge -> "badge"
                            is MessageUi.Content -> "content"
                        }
                    }
                ) {
                    when (it) {
                        is MessageUi.Badge -> {
                            MessageBadge(it)
                        }

                        is MessageUi.Content -> {
                            MessageBox(it)
                        }
                    }
                }
            }

            AnimatedVisibility(
                overlayBadgeContent.isNotEmpty(),
                modifier = Modifier.padding(top = 16.dp),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                MessageBadge(
                    badge = MessageUi.Badge(overlayBadgeContent),
                    isDividerPresent = false,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConversationScreenPreview(
    @PreviewParameter(ConversationUiStatePreviewParameterProvider::class) previewState: PreviewProvider
) {
    HowzAppTheme(
        previewState.isDarkMode
    ) {
        val state by remember { mutableStateOf(previewState.state) }
        val inputState by remember { mutableStateOf(previewState.inputState) }

        ConversationScreenImpl(
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
            onPlayRecordedAudioInPreview = {},
            onPauseRecordedAudioInPreview = {},
            onResumeRecordedAudioInPreview = {},
            onStopRecordedAudioInPreview = {},
            onPlayAudioInPreview = {},
            onPauseAudioInPreview = {},
            onResumeAudioInPreview = {},
            onStopAudioInPreview = {},
            onPlayVideoInPreview = {},
            onPauseVideoInPreview = {},
            onResumeVideoInPreview = {},
            onStopVideoInPreview = {},
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
                add(MessageUi.Badge("Today"))

                addAll(
                    buildList {
                        var mutableMinutes = 4.minutes
                        repeat(10) {
                            val user = listOf(users[0], users[1]).random()
                            val minutes = mutableMinutes + 15.minutes
                            MessageSheet(
                                message = messages.random(),
                                messageId = "m1_${lastIndex + 1}",
                                sender = user,
                                isPictureShowable = it % 3 == 0,
                                messageOwner = messageOwners[if (user.userId == users[0].userId) 0 else 4],
                                timestamp = Clock.System.now().minus(minutes)
                            ).run {
                                add(MessageUi.Content(this))
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
                            messageUis = this
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
                            messageUis = this
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
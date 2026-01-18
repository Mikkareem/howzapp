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
import androidx.compose.foundation.lazy.LazyListState
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
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBadge
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBox
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
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit
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

        val listState = rememberLazyListState()

        val overlayBadgeContent = provideOverlayBadgeContent(state.messageUis, listState)

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
                            MessageBox(
                                it,
                                onImageMessageClick = onImageMessageClick,
                                onVideoMessageClick = onVideoMessageClick
                            )
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

@Composable
private fun provideOverlayBadgeContent(
    messages: List<MessageUi>,
    listState: LazyListState,
): String {
    var overlayBadgeContent by remember { mutableStateOf("") }

    LaunchedEffect(messages, listState) {
        launch {
            snapshotFlow { listState.firstVisibleItemIndex }
                .drop(1)
                .collectLatest {
                    val firstContentIndex = listState.layoutInfo.visibleItemsInfo
                        .firstOrNull { it.contentType == "content" }?.index
                        ?: return@collectLatest

                    val dateString =
                        (messages[firstContentIndex] as MessageUi.Content).content.timestamp.toLocalDateTime(
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

    return overlayBadgeContent
}
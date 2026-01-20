package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessagesList
import com.techullurgy.howzapp.feature.chat.presentation.components.OverlayMessageBadge
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiState

@Composable
internal fun ConversationScreenRoot(
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
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit,
    onLocationMessageClick: (Double, Double) -> Unit
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
                    onDocumentSelected = onDocumentSelected
                )
            }
        }
    ) { padding ->

        val listState = rememberLazyListState()

        Box(
            modifier = Modifier.padding(padding)
        ) {
            MessagesList(
                listState,
                state.messageFeedItems,
                onImageMessageClick,
                onVideoMessageClick,
                onLocationMessageClick
            )

            OverlayMessageBadge(state.messageFeedItems, listState)
        }
    }
}
package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.theme.HowzAppTheme
import com.techullurgy.howzapp.core.designsystem.theme.LocalAppColors
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.presentation.components.InfoBox
import com.techullurgy.howzapp.feature.chat.presentation.components.InputBox
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageBox
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputViewModel
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

data class ConversationKey(
    val conversationId: String
)

@Composable
fun ConversationScreen(
    key: ConversationKey
) {
    val conversationViewModel = koinViewModel<ConversationViewModel> {
        parametersOf(key)
    }

    val conversationInputViewModel = koinViewModel<ConversationInputViewModel> {
        parametersOf(key)
    }

    DisposableEffect(Unit) {
        onDispose {
            conversationViewModel.onAction(ConversationUiAction.SendReadReceiptsIfAny)
        }
    }

    val conversationState by conversationViewModel.state.collectAsState()
    val conversationInputState by conversationInputViewModel.inputState.collectAsState()

    ConversationScreen(
        state = conversationState,
        inputState = conversationInputState,
        onRecordStarted = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStarted) },
        onRecordStopped = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStopped) },
        onMessageSend = { conversationInputViewModel.onAction(ConversationInputUiAction.OnMessageSend) },
        onImageSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnImageSelected(
                    url
                )
            )
        },
        onAudioSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnAudioSelected(
                    url
                )
            )
        },
        onVideoSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnVideoSelected(
                    url
                )
            )
        },
        onDocumentSelected = { name, url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnDocumentSelected(name, url)
            )
        },
        onPlayRecordedAudioInPreview = {
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnPlayRecordedAudio
            )
        },
        onPauseRecordedAudioInPreview = {
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnPauseRecordedAudio
            )
        },
        onResumeRecordedAudioInPreview = {
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnResumeRecordedAudio
            )
        },
        onStopRecordedAudioInPreview = {
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnStopRecordedAudio
            )
        },
        onPlayAudioInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnPlayAudioPreview)
        },
        onPauseAudioInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnPauseAudioPreview)
        },
        onResumeAudioInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnResumeAudioPreview)
        },
        onStopAudioInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnStopAudioPreview)
        },
        onPlayVideoInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnPlayVideoPreview)
        },
        onPauseVideoInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnPauseVideoPreview)
        },
        onResumeVideoInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnResumeVideoPreview)
        },
        onStopVideoInPreview = {
            conversationInputViewModel.onAction(ConversationInputUiAction.OnStopVideoPreview)
        },
    )
}

@Composable
private fun ConversationScreen(
    state: ConversationUiState,
    inputState: ConversationInputUiState,
    onRecordStarted: () -> Unit,
    onRecordStopped: () -> Unit,
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
        modifier = Modifier.fillMaxSize().consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.container1)
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
                modifier = Modifier
                    .padding(8.dp)
            ) {
                InputBox(
                    inputState = inputState,
                    onRecordStarted = onRecordStarted,
                    onRecordStopped = onRecordStopped,
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
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(state.messageSheets) { index, sheet ->
                MessageBox(sheet)

                if(index+1 <= state.messageSheets.lastIndex) {
                    if(state.messageSheets[index+1].isCurrentUser != sheet.isCurrentUser) {
                        Spacer(Modifier.height(16.dp))
                    } else {
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConversationScreenPreview(
    @PreviewParameter(ConversationUiStatePreviewParameterProvider::class) state: ConversationUiState
) {
    HowzAppTheme {
//        ConversationScreen(state)
    }
}

private class ConversationUiStatePreviewParameterProvider :
    PreviewParameterProvider<ConversationUiState> {
    private val sampleMessageSheet = MessageSheet(
        messageId = "m1",
        sender = ChatParticipant("", ""),
        isPictureShowable = false,
        message = OriginalMessage.TextMessage(""),
        messageOwner = MessageOwner.Me(ChatParticipant("", ""), MessageStatus.SenderStatus.SENT),
        timestamp = Clock.System.now()
    )


    override val values: Sequence<ConversationUiState>
        get() = sequenceOf(
            ConversationUiState(
                title = "Irsath Kareem",
                subtitle = "Online",
                messageSheets = listOf(
                    sampleMessageSheet.copy(
                        message = OriginalMessage.ImageMessage(""),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.ImageMessage(""),
                            status = UploadStatus.Progress(28.0)
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Progress(28.0)
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),
                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Failed()
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(28.minutes)
                    ),

                    sampleMessageSheet.copy(
                        message = PendingMessage.UploadablePendingMessage(
                            originalMessage = OriginalMessage.AudioMessage(""),
                            status = UploadStatus.Success("")
                        ),
                        timestamp = sampleMessageSheet.timestamp.minus(3.minutes)
                    ),
                )
            )
        )
}
package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputViewModel
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

data class ConversationKey(
    val conversationId: String
)

@Composable
fun ConversationScreen(
    key: ConversationKey,
    onImagePreview: (String) -> Unit,
    onVideoPreview: (String, String) -> Unit,
    onLocationPreview: (Double, Double) -> Unit
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

    ConversationScreenImpl(
        state = conversationState,
        inputState = conversationInputState,
        onRecordStarted = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStarted) },
        onRecordStopped = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStopped) },
        onRecordCancelled = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordCancelled) },
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
        onImageMessageClick = onImagePreview,
        onVideoMessageClick = { onVideoPreview(key.conversationId, it) },
        onLocationMessageClick = { latitude, longitude ->
            onLocationPreview(latitude, longitude)
        }
    )
}
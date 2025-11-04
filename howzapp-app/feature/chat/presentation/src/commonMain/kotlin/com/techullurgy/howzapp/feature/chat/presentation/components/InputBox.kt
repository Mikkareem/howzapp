package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState

@Composable
internal fun InputBox(
    inputState: ConversationInputUiState,
    modifier: Modifier = Modifier,
    onRecordStarted: () -> Unit,
    onRecordStopped: () -> Unit,
    onMessageSend: () -> Unit,
    onImageSelected: (String) -> Unit,
    onAudioSelected: (String) -> Unit,
    onVideoSelected: (String) -> Unit,
    onDocumentSelected: (String, String) -> Unit,
    onPlayRecordedAudioPreview: () -> Unit,
    onPauseRecordedAudioPreview: () -> Unit,
    onResumeRecordedAudioPreview: () -> Unit,
    onStopRecordedAudioPreview: () -> Unit,
    onPlayAudioPreview: () -> Unit,
    onPauseAudioPreview: () -> Unit,
    onResumeAudioPreview: () -> Unit,
    onStopAudioPreview: () -> Unit,
    onPlayVideoPreview: () -> Unit,
    onPauseVideoPreview: () -> Unit,
    onResumeVideoPreview: () -> Unit,
    onStopVideoPreview: () -> Unit,
) {
    var shouldAdditionBoxOpen by remember { mutableStateOf(false) }

    Column {
        PreviewBox(
            preview = inputState.inputMessagePreview,
            onPlayRecordedAudioPreview = onPlayRecordedAudioPreview,
            onPauseRecordedAudioPreview = onPauseRecordedAudioPreview,
            onResumeRecordedAudioPreview = onResumeRecordedAudioPreview,
            onStopRecordedAudioPreview = onStopRecordedAudioPreview,
            onPlayAudioPreview = onPlayAudioPreview,
            onPauseAudioPreview = onPauseAudioPreview,
            onResumeAudioPreview = onResumeAudioPreview,
            onStopAudioPreview = onStopAudioPreview,
            onPlayVideoPreview = onPlayVideoPreview,
            onPauseVideoPreview = onPauseVideoPreview,
            onResumeVideoPreview = onResumeVideoPreview,
            onStopVideoPreview = onStopVideoPreview,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            TextBox(
                state = inputState.textState,
                canOpenMoreInputSheet = inputState.canOpenMoreInputSheet,
                modifier = Modifier.weight(1f),
                isAdditionBoxOpen = shouldAdditionBoxOpen,
                onAdditionIconClicked = { shouldAdditionBoxOpen = !shouldAdditionBoxOpen }
            )

            InputActionButton(
                canRecordAudio = inputState.canRecordAudio,
                onRecordStarted = onRecordStarted,
                onRecordEnded = onRecordStopped,
                onMessageSend = onMessageSend
            )
        }
    }

    AnimatedVisibility(inputState.canOpenMoreInputSheet && shouldAdditionBoxOpen) {
        InputTypeSelectionBox(
            inputTypes = listOf(
                InputType.Emoji,
                InputType.Sticker,
                InputType.Image,
                InputType.Audio,
                InputType.Video,
                InputType.Attachment,
                InputType.Location,
                InputType.Contact,
            ),
            onInputTypeSelected = {
                when (it) {
                    is InputTypeSelection.OnAttachmentSelected -> onDocumentSelected(
                        it.documentName,
                        it.documentUrl
                    )

                    is InputTypeSelection.OnAudioSelected -> onAudioSelected(it.audioUrl)
                    is InputTypeSelection.OnContactSelected -> {}
                    is InputTypeSelection.OnEmojiSelected -> {}
                    is InputTypeSelection.OnImageSelected -> onImageSelected(it.imageUrl)
                    is InputTypeSelection.OnLocationSelected -> {}
                    is InputTypeSelection.OnStickerSelected -> {}
                    is InputTypeSelection.OnVideoSelected -> onVideoSelected(it.videoUrl)
                }
            }
        )
    }
}
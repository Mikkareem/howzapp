package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiState
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.InputMessagePreview

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
) {
    var shouldAdditionBoxOpen by remember { mutableStateOf(false) }

    Column {
        PreviewBox(
            preview = inputState.inputMessagePreview,
            onPlayRecordedAudioPreview = onPlayRecordedAudioPreview,
            onPauseRecordedAudioPreview = onPauseRecordedAudioPreview,
            onResumeRecordedAudioPreview = onResumeRecordedAudioPreview,
            onStopRecordedAudioPreview = onStopRecordedAudioPreview,
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

@Composable
private fun PreviewBox(
    preview: InputMessagePreview?,
    onPlayRecordedAudioPreview: () -> Unit,
    onPauseRecordedAudioPreview: () -> Unit,
    onResumeRecordedAudioPreview: () -> Unit,
    onStopRecordedAudioPreview: () -> Unit
) {
    AnimatedVisibility(
        visible = preview != null,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = preview!!
        ) {
            when (it) {
                is InputMessagePreview.RecordedAudioPreview -> {
                    RecordedAudioPreviewBox(
                        preview = it,
                        onPlayPreview = onPlayRecordedAudioPreview,
                        onPausePreview = onPauseRecordedAudioPreview,
                        onResumePreview = onResumeRecordedAudioPreview,
                        onStopPreview = onStopRecordedAudioPreview
                    )
                }

                is InputMessagePreview.SelectedAudioPreview -> TODO()
                is InputMessagePreview.SelectedDocumentPreview -> TODO()
                is InputMessagePreview.SelectedImagePreview -> TODO()
                is InputMessagePreview.SelectedVideoPreview -> TODO()
            }
        }
    }
}

@Composable
private fun RecordedAudioPreviewBox(
    preview: InputMessagePreview.RecordedAudioPreview,
    onPlayPreview: () -> Unit,
    onPausePreview: () -> Unit,
    onResumePreview: () -> Unit,
    onStopPreview: () -> Unit
) {
    DisposableEffect(preview) {
        onDispose { onStopPreview() }
    }


}
package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.InputMessagePreview

@Composable
internal fun PreviewBox(
    preview: InputMessagePreview?,
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
    onStopVideoPreview: () -> Unit
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
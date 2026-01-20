package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.InputMessagePreview

@Composable
internal fun PreviewBox(
    preview: InputMessagePreview?
) {
    AnimatedVisibility(
        visible = preview != null,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        AnimatedContent(
            targetState = preview!!
        ) {
            when (it) {
                is InputMessagePreview.RecordedAudioPreview -> {
                    RecordedAudioPreviewBox(preview = it)
                }

                is InputMessagePreview.SelectedAudioPreview -> TODO()
                is InputMessagePreview.SelectedDocumentPreview -> TODO()
                is InputMessagePreview.SelectedImagePreview -> TODO()
                is InputMessagePreview.SelectedVideoPreview -> TODO()
                is InputMessagePreview.SelectedLocationPreview -> TODO()
            }
        }
    }
}
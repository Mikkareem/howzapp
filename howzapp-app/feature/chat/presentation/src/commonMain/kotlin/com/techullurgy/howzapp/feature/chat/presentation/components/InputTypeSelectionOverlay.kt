package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp

@Composable
internal fun InputTypeSelectionOverlay(
    canOpenOverlay: Boolean,
    onImageSelected: (String) -> Unit,
    onAudioSelected: (String) -> Unit,
    onVideoSelected: (String) -> Unit,
    onDocumentSelected: (String, String) -> Unit,
) {
    AnimatedVisibility(
        canOpenOverlay,
        enter = scaleIn(
            // Scale-up from Bottom Left
            transformOrigin = TransformOrigin(0f, 1f)
        ),
        exit = scaleOut(
            // Scale-down to Bottom Left
            transformOrigin = TransformOrigin(0f, 1f)
        )
    ) {
        Box(
            modifier = Modifier
                .dropShadow(RoundedCornerShape(50f)) {
                    spread = 10f
                    alpha = 0.5f
                    radius = 10f
                }
                .background(Color.Green, RoundedCornerShape(50f))
                .padding(8.dp)
        ) {
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
}
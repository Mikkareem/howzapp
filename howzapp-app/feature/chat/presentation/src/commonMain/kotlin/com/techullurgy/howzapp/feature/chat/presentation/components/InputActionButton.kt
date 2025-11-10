package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.unit.dp

@Composable
internal fun InputActionButton(
    canRecordAudio: Boolean,
    isRecording: Boolean,
    onRecordStarted: () -> Unit,
    onRecordEnded: () -> Unit,
    onRecordCancelled: () -> Unit,
    onMessageSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actionState = when {
        canRecordAudio -> InputActionState.Record
        else -> InputActionState.Send
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .dropShadow(CircleShape) {
                spread = 7f
                alpha = 0.5f
                radius = 10f
            }
            .background(MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = actionState
        ) { state ->
            when (state) {
                InputActionState.Record -> {
                    RecordButton(
                        isRecording = isRecording,
                        onRecordStarted = onRecordStarted,
                        onRecordEnded = onRecordEnded,
                        onRecordCancelled = onRecordCancelled
                    )
                }

                InputActionState.Send -> {
                    SendButton(onMessageSend = onMessageSend)
                }
            }
        }
    }
}

private sealed interface InputActionState {
    data object Send : InputActionState
    data object Record : InputActionState
}
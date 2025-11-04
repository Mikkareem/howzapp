package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.core.designsystem.resources.Icons
import howzapp.core.presentation.generated.resources.Res
import howzapp.core.presentation.generated.resources.audio
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun InputActionButton(
    canRecordAudio: Boolean,
    onRecordStarted: () -> Unit,
    onRecordEnded: () -> Unit,
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
                        onRecordStarted = onRecordStarted,
                        onRecordEnded = onRecordEnded
                    )
                }

                InputActionState.Send -> {
                    SendButton(onMessageSend = onMessageSend)
                }
            }
        }
    }
}

@Composable
private fun RecordButton(
    onRecordStarted: () -> Unit,
    onRecordEnded: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isPressedState by interactionSource.collectIsPressedAsState()

    LaunchedEffect(Unit) {
        snapshotFlow { isPressedState }
            .distinctUntilChanged()
            .collectLatest {
                if (it) {
                    onRecordStarted()
                } else {
                    onRecordEnded()
                }
            }
    }

    Icon(
        painter = Icons.recordingIcon,
        contentDescription = "Record Audio",
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                onClick = {}
            )
    )
}

@Composable
private fun SendButton(
    onMessageSend: () -> Unit
) {
    Icon(
        painter = Icons.sendIcon,
        contentDescription = "Send",
        modifier = Modifier
            .clickable(onClick = onMessageSend)
    )
}

private sealed interface InputActionState {
    data object Send : InputActionState
    data object Record : InputActionState
}
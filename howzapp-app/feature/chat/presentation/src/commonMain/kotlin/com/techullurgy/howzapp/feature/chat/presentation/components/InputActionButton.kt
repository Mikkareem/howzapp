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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
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
            .size(60.dp)
            .background(Color.Red)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = actionState,
            modifier = Modifier.matchParentSize()
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
        painter = painterResource(Res.drawable.audio),
        contentDescription = "Record Audio",
        modifier = Modifier.fillMaxSize()
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
        painter = ColorPainter(Color.Green),
        contentDescription = "Send",
        tint = Color.Green,
        modifier = Modifier.fillMaxSize()
            .clickable(onClick = onMessageSend)
    )
}

private sealed interface InputActionState {
    data object Send : InputActionState
    data object Record : InputActionState
}
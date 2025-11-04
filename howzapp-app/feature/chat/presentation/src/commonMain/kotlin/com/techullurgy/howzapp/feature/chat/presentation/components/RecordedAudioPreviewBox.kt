package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.InputMessagePreview

@Composable
internal fun RecordedAudioPreviewBox(
    preview: InputMessagePreview.RecordedAudioPreview,
    onPlayPreview: () -> Unit,
    onPausePreview: () -> Unit,
    onResumePreview: () -> Unit,
    onStopPreview: () -> Unit
) {
    DisposableEffect(preview) {
        onDispose { onStopPreview() }
    }

    val totalDuration = preview.duration
    val durationPlayed = preview.durationPlayed

    val percentagePlayed = durationPlayed.toFloat() / totalDuration.toFloat()

    val playCallback = if (durationPlayed == 0) onPlayPreview else onResumePreview

    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Crossfade(
            targetState = preview.isPlaying
        ) { isPlaying ->
            if (isPlaying) {
                Icon(
                    painter = ColorPainter(Color.Red),
                    contentDescription = "Pause",
                    tint = Color.Red,
                    modifier = Modifier.clip(CircleShape).clickable(onClick = onPausePreview)
                )
            } else {
                Icon(
                    painter = ColorPainter(Color.Green),
                    contentDescription = "Play",
                    tint = Color.Green,
                    modifier = Modifier.clip(CircleShape).clickable(onClick = playCallback)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Slider(value = percentagePlayed, onValueChange = {}, modifier = Modifier.weight(1f))
    }
}
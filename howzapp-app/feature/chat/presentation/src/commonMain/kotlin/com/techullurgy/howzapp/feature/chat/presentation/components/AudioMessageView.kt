package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

@Composable
internal fun AudioMessageView(
    message: OriginalMessage.AudioMessage,
    onPlay: () -> Unit,
    onPause: () -> Unit
) {
    Box(
        modifier = Modifier.size(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = ColorPainter(Color.Magenta),
                    contentDescription = "Sound Icon"
                )
            }
            // ProgressBar
        }
    }
}
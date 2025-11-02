package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

@Composable
internal fun VideoMessageView(
    message: OriginalMessage.VideoMessage
) {
    Box(
        modifier = Modifier.size(250.dp).background(Color.Yellow)
    ) {
        Text(message.videoUrl)
    }
}
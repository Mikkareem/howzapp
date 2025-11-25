package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

@Composable
internal fun VideoMessageView(
    message: OriginalMessage.VideoMessage,
    onVideoMessageClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.clickable { onVideoMessageClick(message.videoUrl) }
    ) {
        Text(message.videoUrl)
    }
}
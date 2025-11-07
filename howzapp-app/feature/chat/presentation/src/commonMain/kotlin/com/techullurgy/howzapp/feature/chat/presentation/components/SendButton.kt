package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.core.designsystem.resources.Icons

@Composable
internal fun SendButton(
    onMessageSend: () -> Unit
) {
    Icon(
        painter = Icons.sendIcon,
        contentDescription = "Send",
        modifier = Modifier
            .clickable(onClick = onMessageSend)
    )
}
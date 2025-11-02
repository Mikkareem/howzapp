package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun EmojiPicker(
    modifier: Modifier = Modifier,
    onEmojiSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier.clickable { onEmojiSelected("") }) {
        content()
    }
}
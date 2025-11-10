package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun StickerPicker(
    modifier: Modifier = Modifier,
    onStickerSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier.clickable { onStickerSelected("") }) {
        content()
    }
}

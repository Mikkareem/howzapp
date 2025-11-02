package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformAudioPicker(
    modifier: Modifier,
    onAudioSelected: (String) -> Unit,
    content: @Composable (() -> Unit)
) {
}
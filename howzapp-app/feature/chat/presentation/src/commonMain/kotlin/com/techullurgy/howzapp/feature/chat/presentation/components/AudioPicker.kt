package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AudioPicker(
    modifier: Modifier = Modifier,
    onAudioSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformAudioPicker(modifier = modifier, onAudioSelected = onAudioSelected, content = content)
}

@Composable
internal expect fun PlatformAudioPicker(
    modifier: Modifier,
    onAudioSelected: (String) -> Unit,
    content: @Composable () -> Unit
)
package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun VideoPicker(
    modifier: Modifier = Modifier,
    onVideoSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformVideoPicker(modifier = modifier, onVideoSelected = onVideoSelected, content = content)
}

@Composable
expect fun PlatformVideoPicker(
    modifier: Modifier,
    onVideoSelected: (String) -> Unit,
    content: @Composable () -> Unit
)
package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformVideoPicker(
    modifier: Modifier,
    onVideoSelected: (String) -> Unit,
    content: @Composable (() -> Unit)
) {
}
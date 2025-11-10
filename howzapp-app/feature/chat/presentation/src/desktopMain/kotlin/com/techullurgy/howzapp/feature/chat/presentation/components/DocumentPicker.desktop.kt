package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformDocumentPicker(
    modifier: Modifier,
    onDocumentSelected: (String) -> Unit,
    content: @Composable (() -> Unit)
) {
}
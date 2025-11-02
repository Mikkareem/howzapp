package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun DocumentPicker(
    modifier: Modifier = Modifier,
    onDocumentSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformDocumentPicker(
        modifier = modifier,
        onDocumentSelected = onDocumentSelected,
        content = content
    )
}

@Composable
expect fun PlatformDocumentPicker(
    modifier: Modifier,
    onDocumentSelected: (String) -> Unit,
    content: @Composable () -> Unit
)
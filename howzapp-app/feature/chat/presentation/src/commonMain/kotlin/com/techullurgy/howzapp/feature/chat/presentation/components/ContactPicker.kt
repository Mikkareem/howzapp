package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ContactPicker(
    modifier: Modifier = Modifier,
    onContactSelected: (String, String) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformContactPicker(
        modifier = modifier,
        onContactSelected = onContactSelected,
        content = content
    )
}

@Composable
internal expect fun PlatformContactPicker(
    modifier: Modifier = Modifier,
    onContactSelected: (String, String) -> Unit,
    content: @Composable () -> Unit
)
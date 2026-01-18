package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun LocationPicker(
    modifier: Modifier = Modifier,
    onLocationSelected: (Double, Double) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformLocationPicker(
        modifier = modifier,
        onLocationSelected = onLocationSelected,
        content = content
    )
}

@Composable
internal expect fun PlatformLocationPicker(
    modifier: Modifier = Modifier,
    onLocationSelected: (Double, Double) -> Unit,
    content: @Composable () -> Unit
)
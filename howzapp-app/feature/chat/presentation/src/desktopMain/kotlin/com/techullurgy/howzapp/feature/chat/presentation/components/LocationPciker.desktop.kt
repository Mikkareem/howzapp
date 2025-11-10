package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformLocationPicker(
    modifier: Modifier,
    onLocationSelected: (Double, Double) -> Unit,
    content: @Composable (() -> Unit)
) {
}
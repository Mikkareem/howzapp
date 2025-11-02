package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformLocationPicker(
    modifier: Modifier,
    onLocationSelected: (Double, Double) -> Unit,
    content: @Composable (() -> Unit)
) {
    Box(modifier.clickable { onLocationSelected(989.99, 783.72) }) {
        content()
    }
}
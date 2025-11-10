package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ImagePicker(
    modifier: Modifier = Modifier,
    onImageSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    PlatformImagePicker(modifier = modifier, onImageSelected = onImageSelected, content = content)
}

@Composable
internal expect fun PlatformImagePicker(
    modifier: Modifier,
    onImageSelected: (String) -> Unit,
    content: @Composable () -> Unit
)
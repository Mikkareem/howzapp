package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformVideoPicker(
    modifier: Modifier,
    onVideoSelected: (String) -> Unit,
    content: @Composable (() -> Unit)
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onVideoSelected(uri.toString())
        }
    )

    Box(
        modifier = modifier.clickable(
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }
        )
    ) {
        content()
    }
}
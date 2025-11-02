package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformContactPicker(
    modifier: Modifier,
    onContactSelected: (String, String) -> Unit,
    content: @Composable (() -> Unit)
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = { uri ->
            onContactSelected(uri.toString(), uri.toString())
        }
    )

    Box(
        modifier = modifier.clickable(
            onClick = {
                launcher.launch()
            }
        )
    ) {
        content()
    }
}
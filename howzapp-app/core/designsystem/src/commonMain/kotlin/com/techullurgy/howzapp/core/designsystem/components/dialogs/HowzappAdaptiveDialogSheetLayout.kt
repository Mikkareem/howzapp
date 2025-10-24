package com.techullurgy.howzapp.core.designsystem.components.dialogs

import androidx.compose.runtime.Composable
import com.techullurgy.howzapp.core.presentation.util.currentDeviceConfiguration

@Composable
fun HowzappAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if(configuration.isMobile) {
        HowzappBottomSheet(
            onDismiss = onDismiss,
            content = content
        )
    } else {
        HowzappDialogContent(
            onDismiss = onDismiss,
            content = content
        )
    }
}
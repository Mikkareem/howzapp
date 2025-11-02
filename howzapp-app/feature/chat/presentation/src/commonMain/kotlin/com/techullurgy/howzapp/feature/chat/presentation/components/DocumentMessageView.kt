package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

@Composable
internal fun DocumentMessageView(
    message: OriginalMessage.DocumentMessage
) {
    Box(
        modifier = Modifier.width(250.dp).height(80.dp).background(Color.Yellow)
    ) {
        Text(message.documentName)
    }
}

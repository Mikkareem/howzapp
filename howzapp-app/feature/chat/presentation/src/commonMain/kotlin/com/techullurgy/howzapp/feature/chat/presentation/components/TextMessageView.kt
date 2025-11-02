package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage

@Composable
internal fun TextMessageView(
    message: OriginalMessage.TextMessage
) {
    Text(message.text)
}
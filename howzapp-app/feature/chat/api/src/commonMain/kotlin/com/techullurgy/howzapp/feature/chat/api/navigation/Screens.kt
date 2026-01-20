package com.techullurgy.howzapp.feature.chat.api.navigation

import androidx.compose.runtime.Composable

data class ConversationKey(
    val conversationId: String
)

interface ConversationScreen {
    @Composable
    operator fun invoke(
        key: ConversationKey,
        onImagePreview: (String) -> Unit,
        onVideoPreview: (String, String) -> Unit,
        onLocationPreview: (Double, Double) -> Unit
    )
}

interface ConversationListScreen {
    @Composable
    operator fun invoke(
        onConversationClick: (String) -> Unit
    )
}

interface ImagePreviewScreen {
    @Composable
    operator fun invoke(
        url: String
    )
}

interface VideoPreviewScreen {
    @Composable
    operator fun invoke(
        listenId: String,
        url: String
    )
}
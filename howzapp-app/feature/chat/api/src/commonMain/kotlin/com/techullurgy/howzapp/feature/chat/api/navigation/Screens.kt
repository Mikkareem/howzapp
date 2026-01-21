package com.techullurgy.howzapp.feature.chat.api.navigation

import androidx.compose.runtime.Composable

data class ConversationKey(
    val conversationId: String
)

interface IConversationScreen {
    @Composable
    operator fun invoke(
        key: ConversationKey,
        onImagePreview: (String) -> Unit,
        onVideoPreview: (String, String) -> Unit,
        onLocationPreview: (Double, Double) -> Unit
    )
}

interface IConversationListScreen {
    @Composable
    operator fun invoke(
        onConversationClick: (String) -> Unit
    )
}

interface IImagePreviewScreen {
    @Composable
    operator fun invoke(
        url: String
    )
}

interface IVideoPreviewScreen {
    @Composable
    operator fun invoke(
        listenId: String,
        url: String
    )
}
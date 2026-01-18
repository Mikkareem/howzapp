package com.techullurgy.howzapp.feature.chat.api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.ConversationKey
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.ConversationScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list.ConversationListScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.media_previews.ImagePreviewScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.media_previews.VideoPreviewScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChatGraphRoute

@Serializable
internal data class ConversationRoute(
    val conversationId: String
)

@Serializable
internal data class ImagePreviewRoute(
    val imageUrl: String
)

@Serializable
internal data class VideoPreviewRoute(
    val videoListenId: String,
    val videoUrl: String
)

fun EntryProviderScope<Any>.chatGraph(
    backStack: SnapshotStateList<Any>
) {
    entry<ChatGraphRoute> {
        ConversationListScreenRoot(
            onConversationClick = { conversationId ->
                backStack.add(
                    ConversationRoute(conversationId)
                )
            }
        )
    }
    entry<ConversationRoute> {
        ConversationScreenRoot(
            route = it,
            onImagePreview = { imageUrl ->
                backStack.add(
                    ImagePreviewRoute(imageUrl)
                )
            },
            onVideoPreview = { id, url ->
                backStack.add(
                    VideoPreviewRoute(id, url)
                )
            },
            onLocationPreview = { latitude, longitude ->

            }
        )
    }
    entry<ImagePreviewRoute> {
        ImagePreviewScreenRoot(
            route = it
        )
    }
    entry<VideoPreviewRoute> {
        VideoPreviewScreenRoot(
            route = it
        )
    }
}

@Composable
private fun ConversationListScreenRoot(
    onConversationClick: (String) -> Unit
) {
    ConversationListScreen(
        onConversationClick = onConversationClick
    )
}

@Composable
private fun ConversationScreenRoot(
    route: ConversationRoute,
    onImagePreview: (String) -> Unit,
    onVideoPreview: (String, String) -> Unit,
    onLocationPreview: (Double, Double) -> Unit
) {
    val key = ConversationKey(route.conversationId)

    ConversationScreen(key, onImagePreview = onImagePreview, onVideoPreview = onVideoPreview, onLocationPreview = onLocationPreview)
}

@Composable
private fun ImagePreviewScreenRoot(
    route: ImagePreviewRoute
) {
    ImagePreviewScreen(route.imageUrl)
}

@Composable
private fun VideoPreviewScreenRoot(
    route: VideoPreviewRoute
) {
    VideoPreviewScreen(route.videoListenId, route.videoUrl)
}
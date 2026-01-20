package com.techullurgy.howzapp.feature.chat.api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import kotlinx.serialization.Serializable
import org.koin.mp.KoinPlatform

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
    KoinPlatform.getKoin().get<ConversationListScreen>().invoke(
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

    KoinPlatform.getKoin().get<ConversationScreen>().invoke(
        key = key,
        onImagePreview = onImagePreview,
        onVideoPreview = onVideoPreview,
        onLocationPreview = onLocationPreview
    )
}

@Composable
private fun ImagePreviewScreenRoot(
    route: ImagePreviewRoute
) {
    KoinPlatform.getKoin().get<ImagePreviewScreen>().invoke(route.imageUrl)
}

@Composable
private fun VideoPreviewScreenRoot(
    route: VideoPreviewRoute
) {
    KoinPlatform.getKoin().get<VideoPreviewScreen>().invoke(route.videoListenId, route.videoUrl)
}
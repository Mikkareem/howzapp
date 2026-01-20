package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageFeedItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun OverlayMessageBadge(
    feedItems: List<MessageFeedItem>,
    listState: LazyListState
) {
    val overlayBadgeContent = provideOverlayBadgeContent(feedItems, listState)

    AnimatedVisibility(
        overlayBadgeContent.isNotEmpty(),
        modifier = Modifier.padding(top = 16.dp),
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        MessageBadge(
            badge = MessageFeedItem.Badge(overlayBadgeContent),
            isDividerPresent = false,
        )
    }
}

@Composable
private fun provideOverlayBadgeContent(
    messages: List<MessageFeedItem>,
    listState: LazyListState,
): String {
    var overlayBadgeContent by remember { mutableStateOf("") }

    LaunchedEffect(messages, listState) {
        launch {
            snapshotFlow { listState.firstVisibleItemIndex }
                .drop(1)
                .collectLatest {
                    val firstContentIndex = listState.layoutInfo.visibleItemsInfo
                        .firstOrNull { it.contentType == "content" }?.index
                        ?: return@collectLatest

                    val dateString =
                        (messages[firstContentIndex] as MessageFeedItem.Content).content.timestamp.toLocalDateTime(
                            TimeZone.UTC
                        ).date

                    overlayBadgeContent = dateString.toString()
                }
        }

        launch {
            snapshotFlow { listState.isScrollInProgress }
                .drop(1)
                .transform {
                    if (!it) {
                        delay(1000)
                        emit(Unit)
                    }
                }
                .collect {
                    overlayBadgeContent = ""
                }
        }
    }

    return overlayBadgeContent
}
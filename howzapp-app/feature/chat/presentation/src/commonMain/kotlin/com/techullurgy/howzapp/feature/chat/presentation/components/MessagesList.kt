package com.techullurgy.howzapp.feature.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.MessageFeedItem

@Composable
internal fun MessagesList(
    listState: LazyListState,
    feedItems: List<MessageFeedItem>,
    onImageMessageClick: (String) -> Unit,
    onVideoMessageClick: (String) -> Unit,
    onLocationMessageClick: (Double, Double) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            feedItems,
            key = {
                when (it) {
                    is MessageFeedItem.Badge -> it.badge
                    is MessageFeedItem.Content -> it.content.messageId
                }
            },
            contentType = {
                when (it) {
                    is MessageFeedItem.Badge -> "badge"
                    is MessageFeedItem.Content -> "content"
                }
            }
        ) {
            when (it) {
                is MessageFeedItem.Badge -> {
                    MessageBadge(it)
                }

                is MessageFeedItem.Content -> {
                    MessageBox(
                        it,
                        onImageMessageClick = onImageMessageClick,
                        onVideoMessageClick = onVideoMessageClick,
                        onLocationMessageClick = onLocationMessageClick
                    )
                }
            }
        }
    }
}
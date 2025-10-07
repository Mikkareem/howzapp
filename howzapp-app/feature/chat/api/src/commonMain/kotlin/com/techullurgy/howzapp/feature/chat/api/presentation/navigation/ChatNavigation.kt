package com.techullurgy.howzapp.feature.chat.api.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderBuilder
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationListScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChatGraphRoute

@Serializable
internal data class ConversationRoute(
    val conversationId: String
)

fun EntryProviderBuilder<Any>.chatGraph(
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
        ConversationScreenRoot(it)
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
    route: ConversationRoute
) {
    val key = ConversationKey(route.conversationId)

    ConversationScreen(key)
}
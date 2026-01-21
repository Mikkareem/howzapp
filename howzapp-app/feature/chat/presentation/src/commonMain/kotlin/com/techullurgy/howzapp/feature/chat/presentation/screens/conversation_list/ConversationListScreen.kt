package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.techullurgy.howzapp.feature.chat.api.navigation.IConversationListScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list.viewmodels.ConversationListViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Factory

@Factory(binds = [IConversationListScreen::class])
internal class DefaultIConversationListScreen : IConversationListScreen {
    @Composable
    override fun invoke(onConversationClick: (String) -> Unit) {
        ConversationListScreen(
            onConversationClick = onConversationClick
        )
    }
}

@Composable
private fun ConversationListScreen(
    onConversationClick: (String) -> Unit
) {
    val viewModel = koinViewModel<ConversationListViewModel>()

    val state by viewModel.state.collectAsState()

    ConversationListScreenRoot(state = state, onConversationClick = onConversationClick)
}
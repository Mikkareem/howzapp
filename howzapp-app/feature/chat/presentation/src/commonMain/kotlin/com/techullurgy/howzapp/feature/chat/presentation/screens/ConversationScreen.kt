package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

data class ConversationKey(
    val conversationId: String
)

@Composable
fun ConversationScreen(
    key: ConversationKey
) {
    val viewModel = koinViewModel<ConversationViewModel> {
        parametersOf(key)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(key.conversationId, style = MaterialTheme.typography.titleLarge)
    }
}
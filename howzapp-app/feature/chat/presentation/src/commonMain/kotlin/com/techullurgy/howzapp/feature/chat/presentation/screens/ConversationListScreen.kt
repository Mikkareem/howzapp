package com.techullurgy.howzapp.feature.chat.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techullurgy.howzapp.feature.chat.presentation.viewmodels.ConversationListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConversationListScreen(
    onConversationClick: (String) -> Unit
) {
    val viewModel = koinViewModel<ConversationListViewModel>()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                onConversationClick("12")
            }
        ) {
            Text("Navigate to 12")
        }
    }
}
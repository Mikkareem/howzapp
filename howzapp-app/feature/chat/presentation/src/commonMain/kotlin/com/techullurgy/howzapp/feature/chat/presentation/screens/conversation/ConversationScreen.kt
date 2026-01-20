package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.techullurgy.howzapp.feature.chat.api.navigation.ConversationKey
import com.techullurgy.howzapp.feature.chat.api.navigation.ConversationScreen
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationInputViewModel
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationUiAction
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels.ConversationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.parameter.parametersOf

@Factory(binds = [ConversationScreen::class])
internal class DefaultConversationScreen : ConversationScreen {
    @Composable
    override operator fun invoke(
        key: ConversationKey,
        onImagePreview: (String) -> Unit,
        onVideoPreview: (String, String) -> Unit,
        onLocationPreview: (Double, Double) -> Unit
    ) {
        ConversationScreen(
            key = key,
            onImagePreview = onImagePreview,
            onVideoPreview = onVideoPreview,
            onLocationPreview = onLocationPreview
        )
    }
}

@Composable
private fun ConversationScreen(
    key: ConversationKey,
    onImagePreview: (String) -> Unit,
    onVideoPreview: (String, String) -> Unit,
    onLocationPreview: (Double, Double) -> Unit
) {
    val conversationViewModel = koinViewModel<ConversationViewModel> {
        parametersOf(key)
    }

    val conversationInputViewModel = koinViewModel<ConversationInputViewModel> {
        parametersOf(key)
    }

    DisposableEffect(Unit) {
        onDispose {
            conversationViewModel.onAction(ConversationUiAction.SendReadReceiptsIfAny)
        }
    }

    val conversationState by conversationViewModel.state.collectAsState()
    val conversationInputState by conversationInputViewModel.inputState.collectAsState()

    ConversationScreenRoot(
        state = conversationState,
        inputState = conversationInputState,
        onRecordStarted = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStarted) },
        onRecordStopped = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordStopped) },
        onRecordCancelled = { conversationInputViewModel.onAction(ConversationInputUiAction.OnAudioRecordCancelled) },
        onMessageSend = { conversationInputViewModel.onAction(ConversationInputUiAction.OnMessageSend) },
        onImageSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnImageSelected(
                    url
                )
            )
        },
        onAudioSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnAudioSelected(
                    url
                )
            )
        },
        onVideoSelected = { url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnVideoSelected(
                    url
                )
            )
        },
        onDocumentSelected = { name, url ->
            conversationInputViewModel.onAction(
                ConversationInputUiAction.OnDocumentSelected(name, url)
            )
        },
        onImageMessageClick = onImagePreview,
        onVideoMessageClick = { onVideoPreview(key.conversationId, it) },
        onLocationMessageClick = { latitude, longitude ->
            onLocationPreview(latitude, longitude)
        }
    )
}
package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation_list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEventType
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetChatPreviewsUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetUserChatEventsUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ConversationListViewModel(
    private val getChatPreviews: GetChatPreviewsUsecase,
    private val getUserChatEvents: GetUserChatEventsUsecase
): ViewModel() {
    private val _state = MutableStateFlow<ConversationListUiState>(ConversationListUiState.Loading)

    val state = _state
        .onStart {
            initialize()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    private fun initialize() {
        combine(
            getChatPreviews(),
            getUserChatEvents()
        ) { previews, events ->

            val uiPreviews = previews.map { preview ->
                val subtitleOverride = events.firstOrNull { it.chatId == preview.chatId }?.let { event ->
                    val eventString = when(event.eventType) {
                        UserChatEventType.TYPING -> "typing..."
                        UserChatEventType.RECORDING_AUDIO -> "recording audio..."
                    }
                    when(val type = preview.chatType) {
                        is ChatType.Direct -> eventString
                        is ChatType.Group -> {
                            val username = type.participants.first { it.userId == event.userId }.username
                            "$username: $eventString"
                        }
                    }
                }

                ChatPreviewUi(
                    preview = preview,
                    subtitleOverride = subtitleOverride
                )
            }

            _state.update {
                ConversationListUiState.Data(
                    previews = uiPreviews
                )
            }
        }.launchIn(viewModelScope)
    }
}


internal sealed interface ConversationListUiState {
    data object Loading: ConversationListUiState

    data class Data(
        val previews: List<ChatPreviewUi>
    ): ConversationListUiState
}

internal data class ChatPreviewUi(
    val preview: ChatPreview,
    val subtitleOverride: String? = null
)
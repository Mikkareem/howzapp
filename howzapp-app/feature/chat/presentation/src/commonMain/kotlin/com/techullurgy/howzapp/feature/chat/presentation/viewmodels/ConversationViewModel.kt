package com.techullurgy.howzapp.feature.chat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ConversationViewModel(
    private val key: ConversationKey,
    private val chatRepository: ChatRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConversationUiState())

    val state = _state
        .onStart {
            observeMessages()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    private fun observeMessages() {
        chatRepository.observeChatByChatId(key.conversationId)
            .onEach { chat ->
                chat ?: return@onEach

                val title = when (val chatType = chat.chatInfo.chatType) {
                    is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.username
                    is ChatType.Group -> chatType.title
                }

                val profileUrl = when (val chatType = chat.chatInfo.chatType) {
                    is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.profilePictureUrl
                    is ChatType.Group -> chatType.pictureUrl
                }

                val subtitle = when (val chatType = chat.chatInfo.chatType) {
                    is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.onlineStatus.run {
                        when (this) {
                            OnlineStatus.IsOnline -> "Online"
                            OnlineStatus.NoOnlineStatus -> ""
                            is OnlineStatus.NotInOnline -> "Last seen $lastSeen"
                        }
                    }

                    is ChatType.Group -> "${chat.chatParticipants.count()} Participants"
                }

                val msgSheets = chat.chatMessages.mapIndexed { index, msg ->
                    MessageSheet(
                        messageId = msg.messageId,
                        sender = msg.owner.owner,
                        messageOwner = msg.owner,
                        isPictureShowable = ({
                            if (index - 1 >= 0) {
                                chat.chatMessages[index - 1].owner.owner.userId != msg.owner.owner.userId
                            } else {
                                true
                            }
                        })(),
                        message = msg.content,
                        timestamp = msg.timestamp
                    )
                }

                _state.value = _state.value.copy(
                    title = title,
                    subtitle = subtitle,
                    profilePicture = profileUrl,
                    messageSheets = _state.value.messageSheets.union(msgSheets).toList()
                )
            }
            .launchIn(viewModelScope)
    }
}

internal data class ConversationUiState(
    val title: String = "",
    val subtitle: String = "",
    val profilePicture: String? = null,
    val messageSheets: List<MessageSheet> = emptyList()
)
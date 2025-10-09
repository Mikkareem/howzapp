package com.techullurgy.howzapp.feature.chat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.OnlineStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.presentation.components.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

internal class ConversationViewModel(
    private val key: ConversationKey,
    private val sessionStorage: SessionStorage,
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
            .combine(sessionStorage.observeAuthInfo()) { chat, authInfo ->
                chat ?: return@combine

                authInfo?.let {
                    val currentUserId = it.user.id

                    val title = when (val chatType = chat.chatInfo.chatType) {
                        is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.username
                        is ChatType.Group -> chatType.title
                    }

                    val profileUrl = when (val chatType = chat.chatInfo.chatType) {
                        is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.profilePictureUrl
                        is ChatType.Group -> chatType.profileUrl
                    }

                    val subtitle = when (val chatType = chat.chatInfo.chatType) {
                        is ChatType.Direct -> chat.chatParticipants.first { p -> p.userId == chatType.other }.onlineStatus.run {
                            when(this) {
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
                            isCurrentUser = msg.owner.owner.userId == currentUserId,
                            isPictureShowable = ({
                                if(index-1 >= 0) {
                                    chat.chatMessages[index-1].owner.owner.userId != msg.owner.owner.userId
                                } else {
                                    true
                                }
                            })(),
                            message = msg.content
                        )
                    }

                    _state.value = _state.value.copy(
                        title = title,
                        subtitle = subtitle,
                        profilePicture = profileUrl,
                        messageSheets = _state.value.messageSheets.union(msgSheets).toList()
                    )
                }
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
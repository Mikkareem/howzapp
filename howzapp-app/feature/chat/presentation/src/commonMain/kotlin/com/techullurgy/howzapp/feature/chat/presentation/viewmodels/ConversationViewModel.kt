package com.techullurgy.howzapp.feature.chat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEventType
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetConversationByIdUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetUserChatEventsByChatIdUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.MarkAsReadForMessageUsecase
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.ConversationKey
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ConversationViewModel(
    private val key: ConversationKey,
    private val getConversationById: GetConversationByIdUsecase,
    private val markAsReadForMessageUsecase: MarkAsReadForMessageUsecase,
    getUserChatEventsByChatId: GetUserChatEventsByChatIdUsecase
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

    private val _userChatEvents = getUserChatEventsByChatId(key.conversationId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun observeMessages() {
        combine(
            getConversationById(key.conversationId),
            _userChatEvents
        ) { chat, chatEvent ->
            chat ?: return@combine

            val title = when (val chatType = chat.chatInfo.chatType) {
                is ChatType.Direct -> chatType.other.username
                is ChatType.Group -> chatType.title
            }

            val profileUrl = when (val chatType = chat.chatInfo.chatType) {
                is ChatType.Direct -> chatType.other.profilePictureUrl
                is ChatType.Group -> chatType.pictureUrl
            }

            var subtitle = when (val chatType = chat.chatInfo.chatType) {
                is ChatType.Direct -> {
                    if(chatType.other.isOnline) "Online"
                    else if(chatType.other.lastSeen != null) "${chatType.other.lastSeen}"
                    else ""
                }
                is ChatType.Group -> "${chatType.participants.count()} Participants"
            }

            if(chatEvent != null) {
                subtitle = when(val chatType = chat.chatInfo.chatType) {
                    is ChatType.Direct -> {
                        when(chatEvent.eventType) {
                            UserChatEventType.TYPING -> "typing..."
                            UserChatEventType.RECORDING_AUDIO -> "recording audio..."
                        }
                    }
                    is ChatType.Group -> {
                        val username = chatType.participants.first { it.userId == chatEvent.userId }.username

                        "$username: " + when(chatEvent.eventType) {
                            UserChatEventType.TYPING -> "typing..."
                            UserChatEventType.RECORDING_AUDIO -> "recording audio..."
                        }
                    }
                }
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
                    .sortedBy { it.timestamp }
            )
        }.launchIn(viewModelScope)
    }

    fun sendReadReceiptsIfAny() {
        viewModelScope.launch(NonCancellable) {
            _state.value.messageSheets
                .filter {
                    (it.messageOwner as? MessageOwner.Other)
                        ?.status in listOf(
                        MessageStatus.ReceiverStatus.UNREAD,
                        MessageStatus.ReceiverStatus.PENDING
                    )
                }.forEach {
                    launch {
                        val messageId = it.messageId
                        markAsReadForMessageUsecase(messageId)
                    }
                }
        }
    }
}

internal data class ConversationUiState(
    val title: String = "",
    val subtitle: String = "",
    val profilePicture: String? = null,
    val messageSheets: List<MessageSheet> = emptyList()
)

internal sealed interface ConversationUiAction {
    class OnSendFile(val bytes: ByteArray) : ConversationUiAction
}
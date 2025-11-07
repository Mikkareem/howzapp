package com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEventType
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetConversationByIdUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.GetUserChatEventsByChatIdUsecase
import com.techullurgy.howzapp.feature.chat.domain.usecases.MarkAsReadForMessageUsecase
import com.techullurgy.howzapp.feature.chat.presentation.models.MessageSheet
import com.techullurgy.howzapp.feature.chat.presentation.screens.conversation.ConversationKey
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ConversationViewModel(
    private val key: ConversationKey,
    private val getConversationById: GetConversationByIdUsecase,
    private val markAsReadForMessage: MarkAsReadForMessageUsecase,
    getUserChatEventsByChatId: GetUserChatEventsByChatIdUsecase
): ViewModel() {
    private val _state = MutableStateFlow(ConversationUiState())

    val state = _state
        .onStart {
            observeMessages()
        }
        .transform {
            // TODO: Group and sort messages, and reassign messageSheets

            emit(it)
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

            chat?.let {
                val title = getTitleOfConversation(chat)
                val profileUrl = getProfilePictureUrlForConversation(chat)
                val subtitle = getSubtitleOfConversation(chat, chatEvent)

                val msgSheets =
                    chat.chatMessages.sortedBy { it.timestamp }.mapIndexed { index, msg ->
                        MessageSheet(
                            messageId = msg.messageId,
                            sender = msg.owner.owner,
                            messageOwner = msg.owner,
                            isPictureShowable = if (index - 1 >= 0) {
                                chat.chatMessages[index - 1].owner.owner.userId != msg.owner.owner.userId
                            } else {
                                true
                            },
                            message = msg.content,
                            timestamp = msg.timestamp
                        )
                    }

                ChatContent(
                    title = title,
                    subtitle = subtitle,
                    profilePicture = profileUrl,
                    messageSheets = msgSheets
                )
            }
        }.onEach { content ->
            content?.let {
                val msgUis = buildList {
                    val unreadMessagesCount = it.messageSheets.count { it.isUnread }
                    val messageSheets = it.messageSheets.sortedBy { it.timestamp }

                    val groupBy =
                        messageSheets.groupBy { s -> s.timestamp.toLocalDateTime(TimeZone.UTC).date }

                    var unreadAdded = false

                    groupBy.forEach { group ->
                        add(MessageUi.Badge(group.key.toString()))

                        group.value.forEach { sheet ->
                            add(MessageUi.Content(sheet))
                        }

                        if (!unreadAdded) {
                            val unreadIndex = group.value.indexOfFirst { it.isUnread }
                            if (unreadIndex != -1) {
                                val desiredIndex = lastIndex - (group.value.lastIndex - unreadIndex)
                                add(
                                    desiredIndex,
                                    MessageUi.Badge("$unreadMessagesCount unread messages")
                                )
                                unreadAdded = true
                            }
                        }
                    }
                }

                _state.value = _state.value.copy(
                    title = it.title,
                    subtitle = it.subtitle,
                    profilePicture = it.profilePicture,
                    messageUis = msgUis
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ConversationUiAction) {
        when (action) {
            ConversationUiAction.SendReadReceiptsIfAny -> sendReadReceiptsIfAny()
        }
    }

    private fun sendReadReceiptsIfAny() {
        viewModelScope.launch {
            withContext(NonCancellable) {
                _state.value.messageUis
                    .filterIsInstance<MessageUi.Content>()
                    .filter { it.content.isUnread }
                    .forEach {
                        launch {
                            val messageId = it.content.messageId
                            markAsReadForMessage(messageId)
                        }
                    }
            }
        }
    }

    private fun getProfilePictureUrlForConversation(chat: Chat): String? {
        return when (val chatType = chat.chatInfo.chatType) {
            is ChatType.Direct -> chatType.other.profilePictureUrl
            is ChatType.Group -> chatType.pictureUrl
        }
    }

    private fun getTitleOfConversation(chat: Chat): String {
        return when (val chatType = chat.chatInfo.chatType) {
            is ChatType.Direct -> chatType.other.username
            is ChatType.Group -> chatType.title
        }
    }

    private fun getSubtitleOfConversation(chat: Chat, chatEvent: UserChatEvent?): String {
        if (chatEvent != null) {
            return when (val chatType = chat.chatInfo.chatType) {
                is ChatType.Direct -> {
                    when (chatEvent.eventType) {
                        UserChatEventType.TYPING -> "typing..."
                        UserChatEventType.RECORDING_AUDIO -> "recording audio..."
                    }
                }

                is ChatType.Group -> {
                    val username =
                        chatType.participants.first { it.userId == chatEvent.userId }.username

                    "$username: " + when (chatEvent.eventType) {
                        UserChatEventType.TYPING -> "typing..."
                        UserChatEventType.RECORDING_AUDIO -> "recording audio..."
                    }
                }
            }
        }

        return when (val chatType = chat.chatInfo.chatType) {
            is ChatType.Direct -> {
                if (chatType.other.isOnline) "Online"
                else if (chatType.other.lastSeen != null) "${chatType.other.lastSeen}"
                else ""
            }

            is ChatType.Group -> "${chatType.participants.count()} Participants"
        }
    }
}

internal data class ConversationUiState(
    val title: String = "",
    val subtitle: String = "",
    val profilePicture: String? = null,
    val messageUis: List<MessageUi> = emptyList()
)

internal sealed interface ConversationUiAction {
    data object SendReadReceiptsIfAny : ConversationUiAction
}

internal sealed interface MessageUi {
    data class Badge(val badge: String) : MessageUi
    data class Content(val content: MessageSheet) : MessageUi
}

private data class ChatContent(
    val title: String,
    val subtitle: String,
    val profilePicture: String?,
    val messageSheets: List<MessageSheet>
)
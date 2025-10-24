package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.auth.User
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

internal class TestRepository(
    appScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val chatLocalRepository: ChatLocalRepository
) {
    init {
        appScope.launch {
            sessionStorage.setAuthInfo(AuthInfo(
                "",
                "",
                User(
                    id = "u1",
                    email = "",
                    username = "Irsath-1",
                    hasVerifiedEmail = false,
                    profilePictureUrl = ""
                )
            ))
            chatLocalRepository.syncChats(chats)
        }
    }
}

private val participants = List(10) {
    ChatParticipant("u${it+1}", "Irsath-${it+1}", "")
}

private val sampleChat = Chat(
    chatInfo = ChatInfo(
        chatId = chatIdFor(0, 1),
        chatType = ChatType.Direct(
            me = participants[0],
            other = participants[1]
        ),
    ),
    chatMessages = listOf(
        ChatMessage(
            messageId = "m1_123",
            chatId = chatIdFor(0,1),
            content = OriginalMessage.TextMessage("Hello How are you?"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(15.seconds)
        )
    )
)

private val chats = listOf(
    sampleChat.copy(
        chatMessages = sampleChat.chatMessages + listOf(
            ChatMessage(
                messageId = "m23871",
                chatId = sampleChat.chatInfo.chatId,
                content = OriginalMessage.TextMessage("Hi, da"),
                owner = MessageOwner.Other(participants[1], MessageStatus.ReceiverStatus.UNREAD),
                timestamp = Clock.System.now().minus(3.seconds)
            ),
            ChatMessage(
                messageId = "m23872",
                chatId = sampleChat.chatInfo.chatId,
                content = OriginalMessage.TextMessage("Hi, di"),
                owner = MessageOwner.Me(participants[0], MessageStatus.SenderStatus.SENT),
                timestamp = Clock.System.now().minus(1.seconds)
            ),
            ChatMessage(
                messageId = "m23873",
                chatId = sampleChat.chatInfo.chatId,
                content = OriginalMessage.AudioMessage(""),
                owner = MessageOwner.Other(participants[1], MessageStatus.ReceiverStatus.UNREAD),
                timestamp = Clock.System.now()
            ),
        )
    )
)

private fun chatIdFor(user1Index: Int, user2Index: Int): String = listOf(participants[user1Index].userId, participants[user2Index].userId).sorted().joinToString("__")
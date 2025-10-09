package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.auth.User
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfo
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OnlineStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class TestRepository(
    appScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val chatLocalRepository: ChatLocalRepository
) {

    init {
        appScope.launch {
            sessionStorage.set(AuthInfo(
                "",
                "",
                User(
                    id = "u123",
                    email = "",
                    username = "Irsath",
                    hasVerifiedEmail = false,
                    profilePictureUrl = ""
                )
            ))
            chatLocalRepository.syncChats(chats)
        }
    }
}

private val sampleChat1 = Chat(
    chatInfo = ChatInfo(
        chatId = "c123",
        chatType = ChatType.Direct(
            me = "u123",
            other = "u234"
        ),
        originator = ChatParticipant(
            userId = "u123",
            username = "Irsath"
        )
    ),
    chatParticipants = listOf(
        ChatParticipant(
            userId = "u123",
            username = "Irsath"
        ),
        ChatParticipant(
            userId = "u234",
            username = "Kareem",
            onlineStatus = OnlineStatus.NotInOnline(lastSeen = Clock.System.now().minus(2.minutes))
        )
    ),
    chatMessages = listOf(
        ChatMessage(
            messageId = "m1_123",
            chatId = "c123",
            content = Message.TextMessage("Hello How are you?"),
            owner = MessageOwner.Me(
                ChatParticipant(
                    userId = "u123",
                    username = "Irsath"
                ),
                MessageStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(15.seconds)
        )
    )
)

private val chats = listOf(
    sampleChat1.copy(
        chatMessages = sampleChat1.chatMessages + listOf(
            sampleChat1.chatMessages.first().copy(
                messageId = "m2_123",
                content = Message.TextMessage("I'm fine, What about you?"),
                owner = MessageOwner.Other(sampleChat1.chatParticipants.last(), false)
            )
        )
    )
)
package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.core.domain.auth.User
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

//private val sampleChat1 = Chat(
//    chatInfo = ChatInfo(
//        chatId = "c123",
//        chatType = ChatType.Direct(
//            me = "u123",
//            other = "u234"
//        ),
//        originator = ChatParticipant(
//            userId = "u123",
//            username = "Irsath"
//        )
//    ),
//    chatParticipants = listOf(
//        ChatParticipant(
//            userId = "u123",
//            username = "Irsath"
//        ),
//        ChatParticipant(
//            userId = "u234",
//            username = "Kareem",
//            onlineStatus = OnlineStatus.NotInOnline(lastSeen = Clock.System.now().minus(2.minutes))
//        )
//    ),
//    chatMessages = listOf(
//        ChatMessage(
//            messageId = "m1_123",
//            chatId = "c123",
//            content = Message.TextMessage("Hello How are you?"),
//            owner = MessageOwner.Me(
//                ChatParticipant(
//                    userId = "u123",
//                    username = "Irsath"
//                ),
//                MessageStatus.DELIVERED
//            ),
//            timestamp = Clock.System.now().minus(15.seconds)
//        )
//    )
//)

private val chats = emptyList<Chat>()
//    listOf(
//    sampleChat1.copy(
//        chatMessages = sampleChat1.chatMessages + listOf(
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m2_123",
//                content = Message.TextMessage("I'm fine, What about you?"),
//                owner = MessageOwner.Other(sampleChat1.chatParticipants.last(), false)
//            ),
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m3_123",
//                content = Message.ImageMessage("https://<public-url-of-an-image>"),
//                owner = MessageOwner.Other(sampleChat1.chatParticipants.last(), false)
//            ),
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m4_123",
//                content = Message.VideoMessage("https://<public-url-of-an-video>"),
//                owner = MessageOwner.Other(sampleChat1.chatParticipants.last(), false)
//            ),
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m5_123",
//                content = Message.DocumentMessage(
//                    "application.pdf",
//                    "https://<public-url-of-an-document>"
//                ),
//                owner = MessageOwner.Me(sampleChat1.chatParticipants.first(), MessageStatus.SENT)
//            ),
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m6_123",
//                content = Message.AudioMessage("https://<public-url-of-an-document>"),
//                owner = MessageOwner.Me(
//                    sampleChat1.chatParticipants.first(),
//                    MessageStatus.DELIVERED
//                )
//            ),
//            sampleChat1.chatMessages.first().copy(
//                messageId = "m7_123",
//                content = Message.AudioMessage("https://<public-url-of-an-video>"),
//                owner = MessageOwner.Other(sampleChat1.chatParticipants.last(), false)
//            )
//        )
//    )
//)
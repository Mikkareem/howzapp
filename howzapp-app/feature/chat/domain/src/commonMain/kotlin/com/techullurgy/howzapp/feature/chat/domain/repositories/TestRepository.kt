package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.auth.AuthInfo
import com.techullurgy.howzapp.core.domain.auth.SessionStorage
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
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
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
                "u1"
            ))
            chatLocalRepository.syncChats(listOf(chat))
        }
    }
}

private val participants = List(10) {
    ChatParticipant(
        userId = "u${it + 1}",
        username = "Irsath-${it + 1}",
        profilePictureUrl = listOf(
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=1160&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ).random()
    )
}

private val chat = Chat(
    chatInfo = ChatInfo(
        chatId = "${participants[0].userId}__${participants[1].userId}",
        chatType = ChatType.Direct(
            me = participants[0],
            other = participants[1]
        ),
    ),
    chatMessages = listOf(

        // ===== December 2024 =====
        ChatMessage(
            messageId = "m1_001",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Hey! How’s everything going lately?"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.READ
            ),
            timestamp = Clock.System.now().minus(330.days)
        ),
        ChatMessage(
            messageId = "m1_002",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Hey! Doing good, just busy with work. You?"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(1.minutes)
        ),
        ChatMessage(
            messageId = "m1_003",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Same here. Deadlines everywhere 😂"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(45.seconds)
        ),
        ChatMessage(
            messageId = "m1_004",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Haha totally get that. Any plans for the holidays?"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(20.seconds)
        ),
        ChatMessage(
            messageId = "m1_403",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Same here. Deadlines everywhere 😂"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(45.seconds)
        ),
        ChatMessage(
            messageId = "m1_404",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.ImageMessage("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=1160&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(20.seconds)
        ),
        ChatMessage(
            messageId = "m1_403",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.VideoMessage("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(45.seconds)
        ),
        ChatMessage(
            messageId = "m1_404",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.VideoMessage("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days).minus(20.seconds)
        ),
        ChatMessage(
            messageId = "m1_005",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.ImageMessage("https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.READ
            ),
            timestamp = Clock.System.now().minus(329.days)
        ),

        // ===== March 2025 =====
        ChatMessage(
            messageId = "m2_001",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Hey, long time! How was your trip?"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(230.days)
        ),
        ChatMessage(
            messageId = "m2_002",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("It was amazing! Got some great photos."),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.UNREAD
            ),
            timestamp = Clock.System.now().minus(230.days).minus(2.minutes)
        ),
        ChatMessage(
            messageId = "m2_003",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Oh nice! Send me a few when you can."),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(230.days).minus(1.minutes)
        ),
        ChatMessage(
            messageId = "m2_004",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Sure thing. I’ll send them tonight."),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.UNREAD
            ),
            timestamp = Clock.System.now().minus(230.days).minus(40.seconds)
        ),
        ChatMessage(
            messageId = "m2_005",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Cool! I’ll be waiting 😄"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(230.days)
        ),

        // ===== July 2025 =====
        ChatMessage(
            messageId = "m3_001",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Hey, you coming to the meetup next week?"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.SENT
            ),
            timestamp = Clock.System.now().minus(120.days)
        ),
        ChatMessage(
            messageId = "m3_002",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Didn’t even know there was one! When is it?"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.PENDING
            ),
            timestamp = Clock.System.now().minus(120.days).minus(1.minutes)
        ),
        ChatMessage(
            messageId = "m3_003",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Saturday at 6. It’s at the new café downtown."),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.SENT
            ),
            timestamp = Clock.System.now().minus(120.days).minus(30.seconds)
        ),
        ChatMessage(
            messageId = "m3_004",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Nice, I’ll try to come!"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.PENDING
            ),
            timestamp = Clock.System.now().minus(120.days).minus(15.seconds)
        ),
        ChatMessage(
            messageId = "m3_005",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Awesome, will be fun to catch up!"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.SENT
            ),
            timestamp = Clock.System.now().minus(120.days)
        ),

        // ===== November 2025 =====
        ChatMessage(
            messageId = "m4_001",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Hey, did you finish the report?"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(1.days).minus(5.minutes)
        ),
        ChatMessage(
            messageId = "m4_002",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Almost done! Just proofreading it."),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.UNREAD
            ),
            timestamp = Clock.System.now().minus(1.days).minus(4.minutes)
        ),
        ChatMessage(
            messageId = "m4_003",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Cool, send it over when you’re done."),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(1.days).minus(3.minutes)
        ),
        ChatMessage(
            messageId = "m4_004",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Will do!"),
            owner = MessageOwner.Other(
                participants[1],
                MessageStatus.ReceiverStatus.UNREAD
            ),
            timestamp = Clock.System.now().minus(1.days).minus(2.minutes)
        ),
        ChatMessage(
            messageId = "m4_005",
            chatId = "${participants[0].userId}__${participants[1].userId}",
            content = OriginalMessage.TextMessage("Thanks 🙌"),
            owner = MessageOwner.Me(
                participants[0],
                MessageStatus.SenderStatus.DELIVERED
            ),
            timestamp = Clock.System.now().minus(1.days).minus(1.minutes)
        ),
    )
)
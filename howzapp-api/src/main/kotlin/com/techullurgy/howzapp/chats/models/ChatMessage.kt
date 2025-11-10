package com.techullurgy.howzapp.chats.models

import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.users.models.AppUser
import java.time.Instant

data class ChatMessage(
    val messageId: MessageId,
    val chatId: ChatId,
    val message: Message,
    val sender: AppUser,
    val status: MessageStatus?,
    val receipt: Receipt?,
    val timestamp: Instant,
)
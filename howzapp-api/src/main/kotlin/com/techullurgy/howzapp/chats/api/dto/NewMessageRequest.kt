package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId

data class NewMessageRequest(
    val chatId: ChatId,
    val localMessageId: MessageId,
    val message: MessageDto,
)
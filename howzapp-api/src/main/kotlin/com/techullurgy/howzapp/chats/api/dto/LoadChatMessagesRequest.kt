package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId

data class LoadChatMessagesRequest(
    val chatId: ChatId,
    val beforeMessage: MessageId,
)

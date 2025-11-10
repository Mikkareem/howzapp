package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.common.types.MessageId

data class NewMessageResponse(
    val localMessageId: MessageId,
    val serverMessageId: MessageId
)

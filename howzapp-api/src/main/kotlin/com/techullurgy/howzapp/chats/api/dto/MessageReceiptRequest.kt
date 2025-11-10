package com.techullurgy.howzapp.chats.api.dto

import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.common.types.MessageId

data class MessageReceiptRequest(
    val messageId: MessageId,
    val receipt: Receipt
)

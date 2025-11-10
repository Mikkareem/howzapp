package com.techullurgy.howzapp.chats.infra.database.projections

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageReceiptsEntity
import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageStatusEntity

data class ChatMessageProjection(
    val message: ChatMessageEntity,
    val status: ChatMessageStatusEntity?,
    val receipt: ChatMessageReceiptsEntity?
)
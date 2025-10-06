package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.relations.ChatDetails
import com.techullurgy.howzapp.feature.chat.domain.models.Chat

fun ChatDetails.toDomain() = Chat(
    chatInfo = chatInfo.toDomain(),
    chatMessages = messages.map { it.toChatMessage() },
    chatParticipants = participants.map { it.toDomain() }
)
package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant

fun ChatParticipantEntity.toDomain() = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)

fun ChatParticipant.toEntity() = ChatParticipantEntity(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)
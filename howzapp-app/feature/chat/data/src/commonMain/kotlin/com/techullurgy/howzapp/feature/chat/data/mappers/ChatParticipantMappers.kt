package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant

fun ChatParticipantEntity.toDomain() = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
    onlineStatus = onlineStatus.toDomain()
)

fun ChatParticipant.toEntity() = ChatParticipantEntity(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
    onlineStatus = onlineStatus.toSerializable()
)
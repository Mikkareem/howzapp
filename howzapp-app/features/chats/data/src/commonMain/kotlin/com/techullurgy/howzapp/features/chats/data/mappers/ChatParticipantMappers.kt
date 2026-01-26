package com.techullurgy.howzapp.features.chats.data.mappers

import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantRelation
import com.techullurgy.howzapp.features.chats.models.ChatParticipant
import kotlin.time.Instant

fun ChatParticipantRelation.toDomain() = ChatParticipant(
    userId = participant.userId,
    username = participant.username,
    profilePictureUrl = participant.profilePictureUrl,
    isOnline = online?.isOnline ?: false,
    lastSeen = online?.lastSeen?.let { Instant.fromEpochMilliseconds(it) }
)

fun ChatParticipant.toEntity() = ChatParticipantEntity(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)
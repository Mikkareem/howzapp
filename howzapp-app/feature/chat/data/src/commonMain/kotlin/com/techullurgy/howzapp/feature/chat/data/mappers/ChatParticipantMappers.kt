package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantRelation
import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
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
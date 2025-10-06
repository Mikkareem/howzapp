package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.howzapp.feature.chat.database.models.SerializableOnlineStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ChatParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val onlineStatus: SerializableOnlineStatus,
    val profilePictureUrl: String? = null,
)
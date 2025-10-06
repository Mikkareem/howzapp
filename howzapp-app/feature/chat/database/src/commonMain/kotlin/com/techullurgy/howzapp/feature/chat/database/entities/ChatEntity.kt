package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.howzapp.feature.chat.database.models.SerializableChatType
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val type: SerializableChatType,
    val originator: String
)

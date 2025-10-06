package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessageStatus
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatMessageEntity::class,
            parentColumns = ["messageId"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("messageId")
    ]
)
data class ChatMessageStatusEntity(
    @PrimaryKey
    val messageId: String,
    val status: SerializableMessageStatus
)

package com.techullurgy.howzapp.features.chats.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.techullurgy.howzapp.features.chats.database.models.SerializableMessage

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("chatId"),
        Index("senderId")
    ]
)
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val senderId: String,
    val message: SerializableMessage,
    val timestamp: Long
)

data class MessageRelation(
    @Embedded val message: MessageEntity,

    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId",
        entity = ChatParticipantEntity::class
    )
    val sender: ChatParticipantRelation,

    @Relation(
        parentColumn = "messageId",
        entityColumn = "messageId",
        entity = StatusEntity::class
    )
    val status: StatusRelation,
)
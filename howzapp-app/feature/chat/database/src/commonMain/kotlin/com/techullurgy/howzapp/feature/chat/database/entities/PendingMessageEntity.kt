package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.techullurgy.howzapp.feature.chat.database.models.SerializablePendingMessage
import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus

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
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class PendingMessageEntity(
    @PrimaryKey val pendingId: String,
    val chatId: String,
    val senderId: String,
    val message: SerializablePendingMessage,
    val isReady: Boolean,
    val timestamp: Long,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PendingMessageEntity::class,
            parentColumns = ["pendingId"],
            childColumns = ["pendingId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("uploadId", unique = true)
    ]
)
data class UploadablePendingMessageEntity(
    @PrimaryKey val pendingId: String,
    val uploadStatus: SerializableUploadStatus
)

data class PendingMessageRelation(
    @Embedded val pending: PendingMessageEntity,

    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId",
    )
    val sender: ChatParticipantEntity,

    @Relation(
        parentColumn = "pendingId",
        entityColumn = "pendingId"
    )
    val uploadable: UploadablePendingMessageEntity?
)
package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
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
data class StatusEntity(
    @PrimaryKey val statusId: String,
    val messageId: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = StatusEntity::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
)
data class SenderStatusEntity(
    @PrimaryKey val statusId: String,
    val status: SenderStatus
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = StatusEntity::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
)
data class ReceiverStatusEntity(
    @PrimaryKey val statusId: String,
    val status: ReceiverStatus
)

enum class SenderStatus {
    PENDING, SENT, DELIVERED, READ
}

enum class ReceiverStatus {
    READ, UNREAD
}

data class StatusRelation(
    @Embedded val status: StatusEntity,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val senderStatus: SenderStatusEntity?,

    @Relation(
        parentColumn = "statusId",
        entityColumn = "statusId",
    )
    val receiverStatus: ReceiverStatusEntity?
)
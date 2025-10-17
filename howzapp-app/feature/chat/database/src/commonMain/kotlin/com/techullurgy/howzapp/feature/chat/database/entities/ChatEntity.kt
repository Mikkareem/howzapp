package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String
)

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
            childColumns = ["meId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["otherId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class DirectChatEntity(
    @PrimaryKey val chatId: String,
    val meId: String,
    val otherId: String
)

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
            childColumns = ["originator"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class GroupChatEntity(
    @PrimaryKey
    val chatId: String,
    val title: String,
    val pictureUrl: String?,
    val originator: String
)

data class DirectChatRelation(
    @Embedded val chat: DirectChatEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "senderId"
    )
    val me: ChatParticipantEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "senderId"
    )
    val other: ChatParticipantEntity
)

data class GroupChatRelation(
    @Embedded val chat: GroupChatEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "originator"
    )
    val originator: ChatParticipantEntity,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "userId",
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantEntity>,
)

data class ChatRelation(
    @Embedded val chat: ChatEntity,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val directChat: DirectChatRelation?,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val groupChat: GroupChatRelation?,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = ChatEntity::class
    )
    val messages: List<MessageRelation>,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = ChatEntity::class
    )
    val pendingMessages: List<PendingMessageRelation>,
)
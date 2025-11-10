package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [
        Index("meId"),
        Index("otherId"),
        Index(value = arrayOf("otherId","meId"), unique = true),
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
    ],
    indices = [
        Index("originator")
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
        parentColumn = "meId",
        entityColumn = "userId",
        entity = ChatParticipantEntity::class
    )
    val me: ChatParticipantRelation,

    @Relation(
        parentColumn = "otherId",
        entityColumn = "userId",
        entity = ChatParticipantEntity::class
    )
    val other: ChatParticipantRelation
)

data class GroupChatRelation(
    @Embedded val chat: GroupChatEntity,

    @Relation(
        parentColumn = "originator",
        entityColumn = "userId",
        entity = ChatParticipantEntity::class
    )
    val originator: ChatParticipantRelation,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "userId",
        entity = ChatParticipantEntity::class,
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantRelation>,
)

data class ChatRelation(
    @Embedded val chat: ChatEntity,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = DirectChatEntity::class
    )
    val directChat: DirectChatRelation?,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = GroupChatEntity::class
    )
    val groupChat: GroupChatRelation?,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = MessageEntity::class
    )
    val messages: List<MessageRelation>,

    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = PendingMessageEntity::class
    )
    val pendingMessages: List<PendingMessageRelation>,
)
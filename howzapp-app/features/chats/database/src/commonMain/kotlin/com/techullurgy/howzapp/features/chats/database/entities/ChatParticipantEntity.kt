package com.techullurgy.howzapp.features.chats.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class ChatParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val profilePictureUrl: String? = null,
)

data class ChatParticipantRelation(
    @Embedded val participant: ChatParticipantEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val online: OnlineUsersEntity?
)
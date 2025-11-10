package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
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
package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId", unique = true)
    ]
)
data class OnlineUsersEntity(
    @PrimaryKey val userId: String,
    val isOnline: Boolean,
    val lastSeen: Long
)
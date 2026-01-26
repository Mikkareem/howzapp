package com.techullurgy.howzapp.features.chats.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.howzapp.features.chats.database.models.SerializableReceipt

@Entity
data class PendingReceiptsEntity(
    @PrimaryKey val id: Long = 0,
    val receipt: SerializableReceipt,
    val isPending: Boolean = true
)

package com.techullurgy.howzapp.feature.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techullurgy.howzapp.feature.chat.database.models.SerializableReceipt

@Entity
data class PendingReceiptsEntity(
    @PrimaryKey val id: Long = 0,
    val receipt: SerializableReceipt,
    val isPending: Boolean = true
)

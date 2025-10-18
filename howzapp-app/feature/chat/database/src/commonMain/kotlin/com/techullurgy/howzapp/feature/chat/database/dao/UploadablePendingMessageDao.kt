package com.techullurgy.howzapp.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.techullurgy.howzapp.feature.chat.database.entities.UploadablePendingMessageEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus

@Dao
interface UploadablePendingMessageDao: BaseDao<UploadablePendingMessageEntity> {
    @Query("UPDATE UploadablePendingMessageEntity SET uploadStatus = :status WHERE pendingId = :uploadId")
    suspend fun updateStatus(uploadId: String, status: SerializableUploadStatus)
}
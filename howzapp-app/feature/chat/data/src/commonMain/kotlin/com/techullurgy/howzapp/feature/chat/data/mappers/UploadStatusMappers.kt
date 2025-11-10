package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus

fun SerializableUploadStatus.toDomain(): UploadStatus {
    return when(this) {
        is SerializableUploadStatus.Cancelled -> UploadStatus.Cancelled(timestamp)
        is SerializableUploadStatus.Failed -> UploadStatus.Failed(timestamp)
        is SerializableUploadStatus.Progress -> UploadStatus.Progress(progressPercentage, timestamp)
        is SerializableUploadStatus.Started -> UploadStatus.Started(timestamp)
        is SerializableUploadStatus.Success -> UploadStatus.Success(publicUrl, timestamp)
        is SerializableUploadStatus.Triggered -> UploadStatus.Triggered(timestamp)
    }
}

fun UploadStatus.toSerializable(): SerializableUploadStatus {
    return when(this) {
        is UploadStatus.Cancelled -> SerializableUploadStatus.Cancelled(timestamp)
        is UploadStatus.Failed -> SerializableUploadStatus.Failed(timestamp)
        is UploadStatus.Progress -> SerializableUploadStatus.Progress(progressPercentage, timestamp)
        is UploadStatus.Started -> SerializableUploadStatus.Started(timestamp)
        is UploadStatus.Success -> SerializableUploadStatus.Success(publicUrl, timestamp)
        is UploadStatus.Triggered -> SerializableUploadStatus.Triggered(timestamp)
    }
}
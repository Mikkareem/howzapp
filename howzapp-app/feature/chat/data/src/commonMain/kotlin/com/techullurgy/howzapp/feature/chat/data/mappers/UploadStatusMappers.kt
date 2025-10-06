package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus

fun SerializableUploadStatus.toDomain(): UploadStatus {
    return when(this) {
        SerializableUploadStatus.Cancelled -> UploadStatus.Cancelled
        SerializableUploadStatus.Failed -> UploadStatus.Failed
        is SerializableUploadStatus.Progress -> UploadStatus.Progress(progressPercentage)
        SerializableUploadStatus.Started -> UploadStatus.Started
        is SerializableUploadStatus.Success -> UploadStatus.Success(publicUrl)
        SerializableUploadStatus.Triggered -> UploadStatus.Triggered
    }
}

fun UploadStatus.toSerializable(): SerializableUploadStatus {
    return when(this) {
        UploadStatus.Cancelled -> SerializableUploadStatus.Cancelled
        UploadStatus.Failed -> SerializableUploadStatus.Failed
        is UploadStatus.Progress -> SerializableUploadStatus.Progress(progressPercentage)
        UploadStatus.Started -> SerializableUploadStatus.Started
        is UploadStatus.Success -> SerializableUploadStatus.Success(publicUrl)
        UploadStatus.Triggered -> SerializableUploadStatus.Triggered
    }
}
package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessage
import com.techullurgy.howzapp.feature.chat.database.models.SerializablePendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage

fun SerializableMessage.toDomain(): OriginalMessage {
    return when(this) {
        is SerializableMessage.TextMessage -> OriginalMessage.TextMessage(text)
        is SerializableMessage.AudioMessage -> OriginalMessage.AudioMessage(audioUrl, optionalText)
        is SerializableMessage.DocumentMessage -> OriginalMessage.DocumentMessage(documentName, documentUrl, optionalText)
        is SerializableMessage.ImageMessage -> OriginalMessage.ImageMessage(imageUrl, optionalText)
        is SerializableMessage.VideoMessage -> OriginalMessage.VideoMessage(videoUrl, optionalText)
    }
}

fun OriginalMessage.toSerializable(): SerializableMessage {
    return when(this) {
        is OriginalMessage.TextMessage -> SerializableMessage.TextMessage(text)
        is OriginalMessage.AudioMessage -> SerializableMessage.AudioMessage(audioUrl, optionalText)
        is OriginalMessage.DocumentMessage -> SerializableMessage.DocumentMessage(documentName, documentUrl, optionalText)
        is OriginalMessage.ImageMessage -> SerializableMessage.ImageMessage(imageUrl, optionalText)
        is OriginalMessage.VideoMessage -> SerializableMessage.VideoMessage(videoUrl, optionalText)
    }
}

fun SerializablePendingMessage.toDomain(): PendingMessage {
    return when(this) {
        is SerializablePendingMessage.NonUploadablePendingMessage -> PendingMessage.NonUploadablePendingMessage(originalMessage.toDomain())
        is SerializablePendingMessage.UploadablePendingMessage -> PendingMessage.UploadablePendingMessage(uploadId,uploadStatus.toDomain(), originalMessage.toDomain())
    }
}

fun PendingMessage.toDomain(): SerializablePendingMessage {
    return when(this) {
        is PendingMessage.NonUploadablePendingMessage -> SerializablePendingMessage.NonUploadablePendingMessage(originalMessage.toSerializable())
        is PendingMessage.UploadablePendingMessage -> SerializablePendingMessage.UploadablePendingMessage(uploadId, status.toSerializable(), originalMessage.toSerializable())
    }
}
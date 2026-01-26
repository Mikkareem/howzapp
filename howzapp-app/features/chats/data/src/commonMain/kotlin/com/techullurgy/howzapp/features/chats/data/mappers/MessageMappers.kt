package com.techullurgy.howzapp.features.chats.data.mappers

import com.techullurgy.howzapp.features.chats.database.models.SerializableMessage
import com.techullurgy.howzapp.features.chats.database.models.SerializablePendingMessage
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.models.PendingMessage

fun SerializableMessage.toDomain(): OriginalMessage {
    return when(this) {
        is SerializableMessage.TextMessage -> OriginalMessage.TextMessage(text)
        is SerializableMessage.AudioMessage -> OriginalMessage.AudioMessage(audioUrl, optionalText)
        is SerializableMessage.DocumentMessage -> OriginalMessage.DocumentMessage(documentName, documentUrl, optionalText)
        is SerializableMessage.ImageMessage -> OriginalMessage.ImageMessage(imageUrl, optionalText)
        is SerializableMessage.VideoMessage -> OriginalMessage.VideoMessage(videoUrl, optionalText)
        is SerializableMessage.LocationMessage -> OriginalMessage.LocationMessage(latitude, longitude)
    }
}

fun OriginalMessage.toSerializable(): SerializableMessage {
    return when(this) {
        is OriginalMessage.TextMessage -> SerializableMessage.TextMessage(text)
        is OriginalMessage.AudioMessage -> SerializableMessage.AudioMessage(audioUrl, optionalText)
        is OriginalMessage.DocumentMessage -> SerializableMessage.DocumentMessage(documentName, documentUrl, optionalText)
        is OriginalMessage.ImageMessage -> SerializableMessage.ImageMessage(imageUrl, optionalText)
        is OriginalMessage.VideoMessage -> SerializableMessage.VideoMessage(videoUrl, optionalText)
        is OriginalMessage.LocationMessage -> SerializableMessage.LocationMessage(latitude, longitude)
    }
}

fun SerializablePendingMessage.toDomain(): PendingMessage {
    return when(this) {
        is SerializablePendingMessage.NonUploadablePendingMessage -> PendingMessage.NonUploadablePendingMessage(originalMessage.toDomain() as OriginalMessage.NonUploadableMessage)
        is SerializablePendingMessage.UploadablePendingMessage -> PendingMessage.UploadablePendingMessage(
            uploadStatus.toDomain(),
            originalMessage.toDomain() as OriginalMessage.UploadableMessage
        )
    }
}

fun PendingMessage.toSerializable(): SerializablePendingMessage {
    return when(this) {
        is PendingMessage.NonUploadablePendingMessage -> SerializablePendingMessage.NonUploadablePendingMessage(originalMessage.toSerializable())
        is PendingMessage.UploadablePendingMessage -> SerializablePendingMessage.UploadablePendingMessage(
            status.toSerializable(),
            originalMessage.toSerializable()
        )
    }
}
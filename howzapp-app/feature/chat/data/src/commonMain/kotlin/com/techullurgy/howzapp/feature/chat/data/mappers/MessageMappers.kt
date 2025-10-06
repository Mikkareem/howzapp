package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessage
import com.techullurgy.howzapp.feature.chat.domain.models.Message

fun SerializableMessage.toDomain(): Message {
    return when(this) {
        is SerializableMessage.TextMessage -> Message.TextMessage(text)
        is SerializableMessage.NonUploadablePendingMessage -> Message.NonUploadablePendingMessage(
            originalMessage.toDomain() as Message.NonUploadableMessage,
            isReadyToSync
        )
        is SerializableMessage.UploadablePendingMessage -> Message.UploadablePendingMessage(
            uploadId,
            uploadStatus.toDomain(),
            originalMessage as Message.UploadableMessage,
            isReadyToSync
        )
        is SerializableMessage.AudioMessage -> Message.AudioMessage(audioUrl, optionalText)
        is SerializableMessage.DocumentMessage -> Message.DocumentMessage(documentName, documentUrl, optionalText)
        is SerializableMessage.ImageMessage -> Message.ImageMessage(imageUrl, optionalText)
        is SerializableMessage.VideoMessage -> Message.VideoMessage(videoUrl, optionalText)
    }
}

fun Message.toSerializable(): SerializableMessage {
    return when(this) {
        is Message.TextMessage -> SerializableMessage.TextMessage(text)
        is Message.NonUploadablePendingMessage -> SerializableMessage.NonUploadablePendingMessage(
            originalMessage.toSerializable() as SerializableMessage.NonUploadableMessage,
            isReadyToSync
        )
        is Message.UploadablePendingMessage -> SerializableMessage.UploadablePendingMessage(
            uploadId,
            originalMessage as SerializableMessage.UploadableMessage,
            status.toSerializable(),
            isReadyToSync
        )
        is Message.AudioMessage -> SerializableMessage.AudioMessage(audioUrl, optionalText)
        is Message.DocumentMessage -> SerializableMessage.DocumentMessage(documentName, documentUrl, optionalText)
        is Message.ImageMessage -> SerializableMessage.ImageMessage(imageUrl, optionalText)
        is Message.VideoMessage -> SerializableMessage.VideoMessage(videoUrl, optionalText)
    }
}
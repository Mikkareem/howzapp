package com.techullurgy.howzapp.feature.chat.database.models

sealed interface SerializablePendingMessage {
    val originalMessage: SerializableMessage

    data class UploadablePendingMessage(
        val uploadId: String,
        val uploadStatus: SerializableUploadStatus,
        override val originalMessage: SerializableMessage
    ): SerializablePendingMessage

    data class NonUploadablePendingMessage(
        override val originalMessage: SerializableMessage
    ): SerializablePendingMessage
}

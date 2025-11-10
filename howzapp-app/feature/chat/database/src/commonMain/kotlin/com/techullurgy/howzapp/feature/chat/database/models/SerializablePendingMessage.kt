package com.techullurgy.howzapp.feature.chat.database.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializablePendingMessage {
    val originalMessage: SerializableMessage

    @Serializable
    data class UploadablePendingMessage(
        val uploadStatus: SerializableUploadStatus,
        override val originalMessage: SerializableMessage
    ): SerializablePendingMessage

    @Serializable
    data class NonUploadablePendingMessage(
        override val originalMessage: SerializableMessage
    ): SerializablePendingMessage
}

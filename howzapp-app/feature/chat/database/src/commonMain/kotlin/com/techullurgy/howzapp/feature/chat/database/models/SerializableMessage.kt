package com.techullurgy.howzapp.feature.chat.database.models

import com.techullurgy.howzapp.feature.chat.database.utils.MessageConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableMessage {

    @Serializable
    sealed interface UploadableMessage: SerializableMessage
    @Serializable
    sealed interface NonUploadableMessage: SerializableMessage

    @Serializable
    sealed interface PendingMessage: SerializableMessage {
        val isReadyToSync: Boolean
    }

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_UPLOADABLE_PENDING_MESSAGE)
    data class UploadablePendingMessage(
        val uploadId: String,
        val originalMessage: UploadableMessage,
        val uploadStatus: SerializableUploadStatus,
        override val isReadyToSync: Boolean
    ): PendingMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_NON_UPLOADABLE_PENDING_MESSAGE)
    data class NonUploadablePendingMessage(
        val originalMessage: NonUploadableMessage,
        override val isReadyToSync: Boolean
    ): PendingMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_TEXT_MESSAGE)
    data class TextMessage(val text: String): NonUploadableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_IMAGE_MESSAGE)
    data class ImageMessage(
        val imageUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_VIDEO_MESSAGE)
    data class VideoMessage(
        val videoUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_AUDIO_MESSAGE)
    data class AudioMessage(
        val audioUrl: String,
        val optionalText: String? = null
    ): UploadableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_DOCUMENT_MESSAGE)
    data class DocumentMessage(
        val documentName: String,
        val documentUrl: String,
        val optionalText: String? = null
    ): UploadableMessage
}
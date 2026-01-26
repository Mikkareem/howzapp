package com.techullurgy.howzapp.features.chats.database.models

import com.techullurgy.howzapp.features.chats.database.utils.MessageConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableMessage {

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_TEXT_MESSAGE)
    data class TextMessage(val text: String) :
        SerializableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_IMAGE_MESSAGE)
    data class ImageMessage(
        val imageUrl: String,
        val optionalText: String? = null
    ): SerializableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_VIDEO_MESSAGE)
    data class VideoMessage(
        val videoUrl: String,
        val optionalText: String? = null
    ): SerializableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_AUDIO_MESSAGE)
    data class AudioMessage(
        val audioUrl: String,
        val optionalText: String? = null
    ): SerializableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_DOCUMENT_MESSAGE)
    data class DocumentMessage(
        val documentName: String,
        val documentUrl: String,
        val optionalText: String? = null
    ): SerializableMessage

    @Serializable
    @SerialName(MessageConstants.DISCRIMINATOR_LOCATION_MESSAGE)
    data class LocationMessage(
        val latitude: Double,
        val longitude: Double,
    ): SerializableMessage
}
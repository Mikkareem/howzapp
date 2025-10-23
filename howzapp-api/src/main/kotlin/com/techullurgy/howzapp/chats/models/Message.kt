package com.techullurgy.howzapp.chats.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(TextMessage ::class, name = "text_message"),
    JsonSubTypes.Type(ImageMessage ::class, name = "image_message"),
    JsonSubTypes.Type(AudioMessage::class, name = "audio_message"),
    JsonSubTypes.Type(VideoMessage::class, name = "video_message"),
    JsonSubTypes.Type(DocumentMessage::class, name = "document_message"),
    JsonSubTypes.Type(StickerMessage::class, name = "sticker_message"),
    JsonSubTypes.Type(DeletedMessage::class, name = "deleted_message"),
)
sealed interface Message

data class TextMessage(
    val text: String,
) : Message

data class ImageMessage(
    val imageUrl: String
) : Message

data class AudioMessage(
    val audioUrl: String,
) : Message

data class VideoMessage(
    val videoUrl: String,
) : Message

data class DocumentMessage(
    val documentUrl: String,
    val documentName: String,
) : Message

data class StickerMessage(
    val stickerUrl: String,
) : Message

data object DeletedMessage: Message
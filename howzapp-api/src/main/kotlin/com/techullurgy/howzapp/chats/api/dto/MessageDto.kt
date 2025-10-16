package com.techullurgy.howzapp.chats.api.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(MessageDto.TextMessageDto::class, name = "text_message"),
    JsonSubTypes.Type(MessageDto.ImageMessageDto::class, name = "image_message"),
    JsonSubTypes.Type(MessageDto.VideoMessageDto::class, name = "video_message"),
    JsonSubTypes.Type(MessageDto.AudioMessageDto::class, name = "audio_message"),
    JsonSubTypes.Type(MessageDto.DocumentMessageDto::class, name = "document_message"),
    JsonSubTypes.Type(MessageDto.ContactMessageDto::class, name = "contact_message"),
)
sealed interface MessageDto {
    data class TextMessageDto(val text: String) : MessageDto
    data class ImageMessageDto(val imageUrl: String) : MessageDto
    data class VideoMessageDto(val videoUrl: String) : MessageDto
    data class AudioMessageDto(val audioUrl: String) : MessageDto
    data class DocumentMessageDto(val documentUrl: String, val documentName: String) : MessageDto
    data class ContactMessageDto(val contactName: String, val contactId: String) : MessageDto
}
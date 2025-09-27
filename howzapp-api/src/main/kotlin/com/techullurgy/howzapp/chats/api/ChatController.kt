package com.techullurgy.howzapp.chats.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.techullurgy.howzapp.chats.services.ChatService
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.uuid.Uuid

@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/new")
    fun newMessage(@RequestBody message: NewMessageRequest): ResponseEntity<String> {
        chatService.onNewMessage(message)
        return ResponseEntity.ok("Sent")
    }
}

data class NewMessageRequest(
    val sender: String,
    val receiver: String,
    val message: MessageDto,
    val receiverType: ReceiverType
)

enum class ReceiverType {
    ONE_TO_ONE_CHAT, GROUP_CHAT
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(MessageDto.TextMessageDto::class, name = "text_message_dto"),
    JsonSubTypes.Type(MessageDto.ImageMessageDto::class, name = "image_message_dto"),
    JsonSubTypes.Type(MessageDto.VideoMessageDto::class, name = "video_message_dto"),
    JsonSubTypes.Type(MessageDto.AudioMessageDto::class, name = "audio_message_dto"),
    JsonSubTypes.Type(MessageDto.DocumentMessageDto::class, name = "document_message_dto"),
    JsonSubTypes.Type(MessageDto.ContactMessageDto::class, name = "contact_message_dto"),
)
sealed interface MessageDto {
    data class TextMessageDto(val text: String): MessageDto
    data class ImageMessageDto(val imageUrl: String): MessageDto
    data class VideoMessageDto(val videoUrl: String): MessageDto
    data class AudioMessageDto(val audioUrl: String): MessageDto
    data class DocumentMessageDto(val documentUrl: String, val documentName: String): MessageDto
    data class ContactMessageDto(val contactName: String, val contactId: String): MessageDto
}
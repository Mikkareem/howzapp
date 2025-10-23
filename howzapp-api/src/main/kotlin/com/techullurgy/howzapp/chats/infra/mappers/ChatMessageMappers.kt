package com.techullurgy.howzapp.chats.infra.mappers

import com.techullurgy.howzapp.chats.infra.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.chats.infra.database.entities.messages.*
import com.techullurgy.howzapp.chats.models.*

fun ChatMessageEntity.toDomain(): Message {
    if (isDeleted) {
        return DeletedMessage
    }

    return when(this) {
        is DocumentMessageEntity -> DocumentMessage(
            documentUrl = documentUrl,
            documentName = documentName
        )
        is AudioMessageEntity -> AudioMessage(
            audioUrl = mediaUrl!!
        )
        is ImageMessageEntity -> ImageMessage(
            imageUrl = mediaUrl!!
        )
        is VideoMessageEntity -> VideoMessage(
            videoUrl = mediaUrl!!
        )
        is StickerMessageEntity -> StickerMessage(
            stickerUrl = stickerUrl
        )
        is TextMessageEntity -> TextMessage(
            text = text
        )
        else -> TODO()
    }
}
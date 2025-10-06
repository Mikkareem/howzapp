package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatMessageStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__full_message",
    value = """
        SELECT  m.messageId, 
                m.chatId,
                m.message,
                m.senderId,
                m.timestamp,
                m.isRead,
                s.messageId AS status_messageId,
                s.status AS status_status,
                p.userId AS sender_userId,
                p.username AS sender_username,
                p.onlineStatus AS sender_onlineStatus,
                p.profilePictureUrl AS sender_profilePictureUrl
        FROM ChatMessageEntity m
        LEFT JOIN ChatMessageStatusEntity s ON s.messageId = m.messageId
        LEFT JOIN ChatParticipantEntity p ON p.userId = m.senderId
    """
)
data class ChatMessageView(
    @Embedded val message: ChatMessageEntity,
    @Embedded(prefix = "status_") val status: ChatMessageStatusEntity?,
    @Embedded(prefix = "sender_") val sender: ChatParticipantEntity
)
package com.techullurgy.howzapp.feature.chat.database.views

import androidx.room.DatabaseView
import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessage
import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessageStatus
import kotlinx.serialization.Serializable

@Serializable
@DatabaseView(
    viewName = "view__last_message_of_the_chat",
    value = """
        SELECT m.chatId, 
                m.messageId,
                m.message AS lastMessage,
                m1.last_message_timestamp AS lastMessageTimestamp,
                m1.unread_messages_count AS unreadMessagesCount,
                s.status AS messageStatus
        FROM ChatMessageEntity m
        JOIN (
            SELECT chatId, MAX(timestamp) AS last_message_timestamp,
                COUNT(CASE WHEN isRead = false THEN 1 END) AS unread_messages_count
            FROM ChatMessageEntity
            GROUP BY chatId
        ) m1
            ON m1.chatId = m.chatId AND m1.last_message_timestamp = m.timestamp
        LEFT JOIN ChatMessageStatusEntity s
            ON s.messageId = m.messageId
    """
)
data class ChatLastMessageView(
    val chatId: String,
    val messageId: String,
    val lastMessage: SerializableMessage,
    val lastMessageTimestamp: Long,
    val unreadMessagesCount: Int,
    val messageStatus: SerializableMessageStatus?
)

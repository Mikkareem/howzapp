package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfoWithLastMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatLocalRepository {
    fun observeChatInfoWithLastMessage(): Flow<List<ChatInfoWithLastMessage>>
    fun observeChatDetailsById(chatId: String): Flow<Chat?>

    suspend fun syncChats(chats: List<Chat>)
    suspend fun updateMessage(message: ChatMessage)
    suspend fun updateMessageAsSync(oldMessageId: String, message: ChatMessage)
}
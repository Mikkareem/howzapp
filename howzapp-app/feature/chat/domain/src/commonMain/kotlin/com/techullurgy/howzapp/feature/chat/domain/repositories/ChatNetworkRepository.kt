package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage

interface ChatNetworkRepository {
    fun sendMessage(message: ChatMessage): AppResult<String, DataError>
    fun sendReadReceiptToMessage(message: ChatMessage)
    suspend fun syncChats(): AppResult<List<Chat>, DataError.Remote> // Sync Requests from websockets

    fun fetchChatMessages(chatId: String, before: Long? = null) // Pagination
}
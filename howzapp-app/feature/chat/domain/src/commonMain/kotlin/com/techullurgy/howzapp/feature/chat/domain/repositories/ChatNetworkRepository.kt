package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.NetworkSyncChatsResult

interface ChatNetworkRepository {
    fun sendMessage(message: ChatMessage): AppResult<String, DataError>
    suspend fun syncChats(lastSyncTimestamp: Long): AppResult<NetworkSyncChatsResult, DataError.Remote> // Sync Requests from websockets
    fun fetchChatMessages(chatId: String, before: Long? = null) // Pagination

    suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
    suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
}
package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.networking.models.NetworkLoadMessagesResult
import com.techullurgy.howzapp.feature.chat.domain.networking.models.NetworkSyncChatsResult

interface ChatNetworkRepository {
    suspend fun sendMessage(message: ChatMessage): AppResult<String, DataError>
    suspend fun syncChats(lastSyncTimestamp: Long): AppResult<NetworkSyncChatsResult, DataError.Remote> // Sync Requests from websockets
    suspend fun fetchChatMessages(chatId: String, beforeMessage: String): AppResult<NetworkLoadMessagesResult, DataError.Remote>

    suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
    suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
}
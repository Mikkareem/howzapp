package com.techullurgy.howzapp.features.chats.domain.repositories

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import com.techullurgy.howzapp.common.utils.models.EmptyResult
import com.techullurgy.howzapp.features.chats.models.ChatMessage
import com.techullurgy.howzapp.features.chats.results.NetworkLoadMessagesResult
import com.techullurgy.howzapp.features.chats.results.NetworkSyncChatsResult

interface ChatNetworkRepository {
    suspend fun sendMessage(message: ChatMessage): AppResult<String, DataError>

    // Sync Requests from websockets
    suspend fun syncChats(lastSyncTimestamp: Long): AppResult<NetworkSyncChatsResult, DataError.Remote>
    suspend fun fetchChatMessages(
        chatId: String,
        beforeMessage: String
    ): AppResult<NetworkLoadMessagesResult, DataError.Remote>

    suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
    suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote>
}
package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.core.data.networking.get
import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import com.techullurgy.howzapp.core.domain.util.map
import com.techullurgy.howzapp.feature.chat.data.dto.requests.SyncRequest
import com.techullurgy.howzapp.feature.chat.data.dto.responses.SyncResponse
import com.techullurgy.howzapp.feature.chat.data.mappers.toDomain
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.NetworkSyncChatsResult
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [ChatNetworkRepository::class])
internal class DefaultChatNetworkRepository(
    private val client: HttpClient
): ChatNetworkRepository {
    override fun sendMessage(message: ChatMessage): AppResult<String, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun syncChats(lastSyncTimestamp: Long): AppResult<NetworkSyncChatsResult, DataError.Remote> {
        return client.get<SyncRequest, SyncResponse>(
            route = "/api/chats/sync",
            body = SyncRequest(lastSyncTimestamp)
        ).map {
            NetworkSyncChatsResult(
                chats = it.chats.map { dto ->
                    dto.toDomain()
                },
                lastSyncTimestamp = it.lastSyncTimestamp
            )
        }
    }

    override fun fetchChatMessages(chatId: String, before: Long?) {
        TODO("Not yet implemented")
    }
}
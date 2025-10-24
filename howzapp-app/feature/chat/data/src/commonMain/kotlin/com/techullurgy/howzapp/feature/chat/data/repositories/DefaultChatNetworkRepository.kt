package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.core.data.networking.get
import com.techullurgy.howzapp.core.data.networking.post
import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.core.domain.util.EmptyResult
import com.techullurgy.howzapp.core.domain.util.asEmptyResult
import com.techullurgy.howzapp.core.domain.util.map
import com.techullurgy.howzapp.feature.chat.data.dto.models.ReceiptDto
import com.techullurgy.howzapp.feature.chat.data.dto.requests.LoadChatMessagesRequest
import com.techullurgy.howzapp.feature.chat.data.dto.requests.MessageReceiptRequest
import com.techullurgy.howzapp.feature.chat.data.dto.requests.NewMessageRequest
import com.techullurgy.howzapp.feature.chat.data.dto.requests.SyncRequest
import com.techullurgy.howzapp.feature.chat.data.dto.responses.LoadChatMessagesResponse
import com.techullurgy.howzapp.feature.chat.data.dto.responses.NewMessageResponse
import com.techullurgy.howzapp.feature.chat.data.dto.responses.SyncResponse
import com.techullurgy.howzapp.feature.chat.data.mappers.toDomain
import com.techullurgy.howzapp.feature.chat.data.mappers.toDto
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.networking.models.NetworkLoadMessagesResult
import com.techullurgy.howzapp.feature.chat.domain.networking.models.NetworkSyncChatsResult
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single(binds = [ChatNetworkRepository::class])
internal class DefaultChatNetworkRepository(
    private val client: HttpClient
): ChatNetworkRepository {
    override suspend fun sendMessage(message: ChatMessage): AppResult<String, DataError> {
        return client.post<NewMessageRequest, NewMessageResponse>(
            route = "/api/chat/message",
            body = NewMessageRequest(
                chatId = message.chatId,
                localMessageId = message.messageId,
                message = (message.content as OriginalMessage).toDto()
            )
        ).map { it.serverMessageId }
    }

    override suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        return client.post<MessageReceiptRequest, Unit>(
            route = "/api/chats/receipt",
            body = MessageReceiptRequest(
                messageId = messageId,
                receipt = ReceiptDto.READ
            )
        ).asEmptyResult()
    }

    override suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        return client.post<MessageReceiptRequest, Unit>(
            route = "/api/chats/receipt",
            body = MessageReceiptRequest(
                messageId = messageId,
                receipt = ReceiptDto.DELIVERED
            )
        ).asEmptyResult()
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

    override suspend fun fetchChatMessages(chatId: String, beforeMessage: String): AppResult<NetworkLoadMessagesResult, DataError.Remote> {
        return client.get<LoadChatMessagesRequest, LoadChatMessagesResponse>(
            route = "/api/chats/load",
            body = LoadChatMessagesRequest(chatId, beforeMessage)
        ).map {
            NetworkLoadMessagesResult(
                messages = it.messages.map { m -> m.toDomain() },
                hasPreviousAvailable = it.hasPreviousAvailable,
            )
        }
    }
}
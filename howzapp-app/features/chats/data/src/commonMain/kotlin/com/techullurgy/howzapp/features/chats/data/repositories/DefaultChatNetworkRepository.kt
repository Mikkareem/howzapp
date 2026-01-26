package com.techullurgy.howzapp.features.chats.data.repositories

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import com.techullurgy.howzapp.common.utils.models.EmptyResult
import com.techullurgy.howzapp.common.utils.models.asEmptyResult
import com.techullurgy.howzapp.common.utils.models.map
import com.techullurgy.howzapp.core.data.api.AppHttpConnector
import com.techullurgy.howzapp.features.chats.data.dto.ReceiptDto
import com.techullurgy.howzapp.features.chats.data.mappers.toDomain
import com.techullurgy.howzapp.features.chats.data.mappers.toDto
import com.techullurgy.howzapp.features.chats.data.requests.MessageReceiptRequest
import com.techullurgy.howzapp.features.chats.data.requests.NewMessageRequest
import com.techullurgy.howzapp.features.chats.data.responses.LoadChatMessagesResponse
import com.techullurgy.howzapp.features.chats.data.responses.NewMessageResponse
import com.techullurgy.howzapp.features.chats.data.responses.SyncResponse
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.features.chats.models.ChatMessage
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.results.NetworkLoadMessagesResult
import com.techullurgy.howzapp.features.chats.results.NetworkSyncChatsResult
import org.koin.core.annotation.Single

@Single
internal class DefaultChatNetworkRepository(
    private val client: AppHttpConnector
): ChatNetworkRepository {
    override suspend fun sendMessage(message: ChatMessage): AppResult<String, DataError> {
        return client.post<NewMessageRequest, NewMessageResponse>(
            route = "/api/chats/message/new",
            body = NewMessageRequest(
                chatId = message.chatId,
                localMessageId = message.messageId,
                message = (message.content as OriginalMessage).toDto()
            )
        ).map { it.serverMessageId }
    }

    override suspend fun sendReadReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        return client.put<MessageReceiptRequest, Unit>(
            route = "/api/chats/receipt",
            body = MessageReceiptRequest(
                messageId = messageId,
                receipt = ReceiptDto.READ
            )
        ).asEmptyResult()
    }

    override suspend fun sendDeliveryReceiptToMessage(messageId: String): EmptyResult<DataError.Remote> {
        return client.put<MessageReceiptRequest, Unit>(
            route = "/api/chats/receipt",
            body = MessageReceiptRequest(
                messageId = messageId,
                receipt = ReceiptDto.DELIVERED
            )
        ).asEmptyResult()
    }

    override suspend fun syncChats(lastSyncTimestamp: Long): AppResult<NetworkSyncChatsResult, DataError.Remote> {
        return client.get<SyncResponse>(
            route = "/api/chats/sync",
            queryParams = mapOf("lastSyncTimestamp" to lastSyncTimestamp.toString())
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
        return client.get<LoadChatMessagesResponse>(
            route = "/api/chats/load",
            queryParams = mapOf("chatId" to chatId, "beforeMessage" to beforeMessage)
        ).map {
            NetworkLoadMessagesResult(
                messages = it.messages.map { m -> m.toDomain() },
                hasPreviousAvailable = it.hasPreviousAvailable,
            )
        }
    }
}
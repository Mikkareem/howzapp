package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import org.koin.core.annotation.Single

@Single(binds = [ChatNetworkRepository::class])
internal class DefaultChatNetworkRepository: ChatNetworkRepository {
    override fun sendMessage(message: ChatMessage): AppResult<ChatMessage, DataError> {
        TODO("Not yet implemented")
    }

    override fun sendReadReceiptToMessage(message: ChatMessage) {
        TODO("Not yet implemented")
    }

    override fun syncChats(): AppResult<List<Chat>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override fun uploadMessage(
        message: ChatMessage,
        onSuccess: suspend (String) -> Unit
    ): AppResult<String, DataError> {
        TODO("Not yet implemented")
    }

    override fun fetchChatMessages(chatId: String, before: Long?) {
        TODO("Not yet implemented")
    }
}
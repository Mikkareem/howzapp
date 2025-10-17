package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import kotlinx.coroutines.flow.Flow

interface ChatLocalRepository {
    fun observeChatWithLastMessageAndUnreadCount(): Flow<List<Chat>>

    fun observeConversation(chatId: String): Flow<Chat?>

    // This will upload to the server, update necessary fields, and insert into the MessageEntity
    fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatMessage>>

    // This will upload to the server, and mark pending message as ReadyToSync
    fun observeUploadableMessagesThatAreReadyToUpload(): Flow<List<ChatMessage>>

    // This will cancel ongoing upload (if any), and delete the message
    fun observeUploadableMessagesThatAreCancelled(): Flow<List<ChatMessage>>

    suspend fun syncChats(chats: List<Chat>): AppResult<Unit, DataError.Local>

    suspend fun newPendingMessage(message: OriginalMessage): AppResult<Boolean, DataError.Local>

    suspend fun updateStatusOfUpload(uploadId: String, status: UploadStatus)
}
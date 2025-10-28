package com.techullurgy.howzapp.feature.chat.domain.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingReceipt
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import kotlinx.coroutines.flow.Flow

interface ChatLocalRepository {
    fun observeChatWithLastMessageAndUnreadCount(): Flow<List<ChatPreview>>

    fun observeConversation(chatId: String): Flow<Chat?>

    // This will upload to the server, update necessary fields, and insert into the MessageEntity
    fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatMessage>>

    // This will upload to the server, and mark pending message as Success
    fun observeUploadableMessagesThatAreReadyToUpload(): Flow<List<ChatMessage>>

    // This will cancel ongoing upload (if any), and delete the message
    fun observeUploadableMessagesThatAreCancelled(): Flow<List<ChatMessage>>

    fun observePendingReceipts(): Flow<List<PendingReceipt>>

    fun observeAllParticipants(): Flow<List<String>>

    fun observeAllChats(): Flow<List<String>>

    suspend fun deletePendingMessage(pendingId: String)

    suspend fun syncChats(chats: List<Chat>): AppResult<Unit, DataError.Local>

    suspend fun newPendingMessage(chatId: String, senderId: String, message: PendingMessage): AppResult<Boolean, DataError.Local>

    suspend fun messageSentAndDeletePendingMessage(pendingId: String, message: ChatMessage): AppResult<Boolean, DataError.Local>

    suspend fun updateStatusOfUpload(uploadId: String, status: UploadStatus)

    suspend fun updateUploadablePendingMessageAsReady(pendingId: String, publicUrl: String)

    suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean)

    suspend fun updatePendingReceiptAsCompleted(id: Long)

    suspend fun markMessageAsRead(messageId: String)

    suspend fun reset()
}
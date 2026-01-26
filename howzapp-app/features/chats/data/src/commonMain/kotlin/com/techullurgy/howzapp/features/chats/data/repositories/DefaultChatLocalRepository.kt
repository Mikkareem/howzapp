package com.techullurgy.howzapp.features.chats.data.repositories

import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError
import com.techullurgy.howzapp.core.database.AppDatabase
import com.techullurgy.howzapp.features.chats.data.mappers.toDomain
import com.techullurgy.howzapp.features.chats.data.mappers.toSerializable
import com.techullurgy.howzapp.features.chats.database.dao.ChatDao
import com.techullurgy.howzapp.features.chats.database.dao.DirectChatDao
import com.techullurgy.howzapp.features.chats.database.dao.GroupChatDao
import com.techullurgy.howzapp.features.chats.database.dao.MessageDao
import com.techullurgy.howzapp.features.chats.database.dao.OnlineUsersDao
import com.techullurgy.howzapp.features.chats.database.dao.ParticipantsCrossRefDao
import com.techullurgy.howzapp.features.chats.database.dao.ParticipantsDao
import com.techullurgy.howzapp.features.chats.database.dao.PendingMessageDao
import com.techullurgy.howzapp.features.chats.database.dao.PendingReceiptsDao
import com.techullurgy.howzapp.features.chats.database.dao.ReceiverStatusDao
import com.techullurgy.howzapp.features.chats.database.dao.SenderStatusDao
import com.techullurgy.howzapp.features.chats.database.dao.StatusDao
import com.techullurgy.howzapp.features.chats.database.dao.UploadablePendingMessageDao
import com.techullurgy.howzapp.features.chats.database.entities.ChatEntity
import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.features.chats.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.features.chats.database.entities.DirectChatEntity
import com.techullurgy.howzapp.features.chats.database.entities.GroupChatEntity
import com.techullurgy.howzapp.features.chats.database.entities.MessageEntity
import com.techullurgy.howzapp.features.chats.database.entities.OnlineUsersEntity
import com.techullurgy.howzapp.features.chats.database.entities.PendingMessageEntity
import com.techullurgy.howzapp.features.chats.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.features.chats.database.entities.ReceiverStatusEntity
import com.techullurgy.howzapp.features.chats.database.entities.SenderStatus
import com.techullurgy.howzapp.features.chats.database.entities.SenderStatusEntity
import com.techullurgy.howzapp.features.chats.database.entities.StatusEntity
import com.techullurgy.howzapp.features.chats.database.entities.UploadablePendingMessageEntity
import com.techullurgy.howzapp.features.chats.database.models.SerializableMessage
import com.techullurgy.howzapp.features.chats.database.models.SerializablePendingMessage
import com.techullurgy.howzapp.features.chats.database.models.SerializableReceipt
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.models.Chat
import com.techullurgy.howzapp.features.chats.models.ChatMessage
import com.techullurgy.howzapp.features.chats.models.ChatPreview
import com.techullurgy.howzapp.features.chats.models.ChatType
import com.techullurgy.howzapp.features.chats.models.MessageOwner
import com.techullurgy.howzapp.features.chats.models.MessageStatus
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.models.PendingMessage
import com.techullurgy.howzapp.features.chats.models.PendingReceipt
import com.techullurgy.howzapp.features.chats.models.UploadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single
internal class DefaultChatLocalRepository(
    private val chatDao: ChatDao,
    private val directChatDao: DirectChatDao,
    private val groupChatDao: GroupChatDao,
    private val participantsCrossRefDao: ParticipantsCrossRefDao,
    private val pendingMessageDao: PendingMessageDao,
    private val pendingReceiptsDao: PendingReceiptsDao,
    private val uploadablePendingMessageDao: UploadablePendingMessageDao,
    private val messageDao: MessageDao,
    private val participantsDao: ParticipantsDao,
    private val statusDao: StatusDao,
    private val senderStatusDao: SenderStatusDao,
    private val receiverStatusDao: ReceiverStatusDao,
    private val onlineUsersDao: OnlineUsersDao,
    private val database: AppDatabase,
): ChatLocalRepository {
    override suspend fun newPendingMessage(
        chatId: String,
        senderId: String,
        message: PendingMessage
    ): AppResult<Boolean, DataError.Local> {
        database.safeExecute {

            if (chatId.contains("__") && chatDao.getChatById(chatId).firstOrNull() == null) {
                chatDao.upsert(ChatEntity(chatId = chatId))

                directChatDao.upsert(
                    DirectChatEntity(
                        chatId = chatId,
                        meId = senderId,
                        otherId = chatId.split("__").first { it != senderId }
                    )
                )

                chatId.split("__").forEach {
                    participantsCrossRefDao.upsert(
                        ChatParticipantCrossRef(
                            chatId = chatId,
                            userId = it
                        )
                    )
                }
            }

            when(message) {
                is PendingMessage.NonUploadablePendingMessage -> {
                    pendingMessageDao.upsert(
                        PendingMessageEntity(
                            pendingId = randomId(),
                            chatId = chatId,
                            senderId = senderId,
                            message = message.toSerializable(),
                            isReady = true,
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    )
                }
                is PendingMessage.UploadablePendingMessage -> {
                    val pendingMessage = PendingMessageEntity(
                        pendingId = randomId(),
                        chatId = chatId,
                        senderId = senderId,
                        message = message.toSerializable(),
                        isReady = false,
                        timestamp = Clock.System.now().toEpochMilliseconds()
                    )

                    pendingMessageDao.upsert(pendingMessage)

                    uploadablePendingMessageDao.upsert(
                        UploadablePendingMessageEntity(
                            pendingId = pendingMessage.pendingId,
                            uploadStatus = message.status.toSerializable()
                        )
                    )
                }
            }
        }

        return AppResult.Success(true)
    }

    override suspend fun messageSentAndDeletePendingMessage(pendingId: String, message: ChatMessage): AppResult<Boolean, DataError.Local> {
        database.safeExecute {
            messageDao.upsert(
                MessageEntity(
                    messageId = message.messageId,
                    chatId = message.chatId,
                    senderId = message.owner.owner.userId,
                    message = (message.content as OriginalMessage).toSerializable(),
                    timestamp = message.timestamp.toEpochMilliseconds()
                )
            )

            val status = StatusEntity(
                statusId = message.messageId, //Uuid.random().toString(),
                messageId = message.messageId
            )

            statusDao.upsert(status)

            senderStatusDao.upsert(
                SenderStatusEntity(
                    statusId = status.statusId,
                    status = SenderStatus.SENT
                )
            )

            deletePendingMessage(pendingId)
        }

        return AppResult.Success(true)
    }

    override fun observeChatWithLastMessageAndUnreadCount(): Flow<List<ChatPreview>> {
        return chatDao.getAllChats()
            .distinctUntilChanged()
            .map { chatRelations ->
                chatRelations.map { it.toDomain() }
            }
            .map { chats ->
                chats.map {
                    ChatPreview(
                        chatId = it.chatInfo.chatId,
                        title = when (val type = it.chatInfo.chatType) {
                            is ChatType.Direct -> type.other.username
                            is ChatType.Group -> type.title
                        },
                        picture = when (val type = it.chatInfo.chatType) {
                            is ChatType.Direct -> type.other.profilePictureUrl
                            is ChatType.Group -> type.pictureUrl
                        },
                        unreadCount = it.chatMessages
                            .count { msg ->
                                val owner = msg.owner as? MessageOwner.Other ?: return@count false
                                owner.status == MessageStatus.ReceiverStatus.UNREAD
                            },
                        chatType = it.chatInfo.chatType,
                        lastMessage = it.chatMessages.last(),
                        lastMessageTimestamp = it.chatMessages.last().timestamp
                    )
                }
            }
    }

    override fun observeConversation(chatId: String): Flow<Chat?> {
        return chatDao.getChatById(chatId)
            .distinctUntilChanged()
            .map {
                it?.toDomain()
            }
    }

    override fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatMessage>> {
        return pendingMessageDao.getPendingMessagesThatAreReadyToSync()
            .distinctUntilChanged()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override fun observeUploadableMessagesThatAreReadyToUpload(): Flow<List<ChatMessage>> {
        return pendingMessageDao.getUploadablePendingMessagesThatAreTriggered()
            .distinctUntilChanged()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override fun observeUploadableMessagesThatAreCancelled(): Flow<List<ChatMessage>> {
        return pendingMessageDao.getUploadablePendingMessagesThatAreCancelled()
            .distinctUntilChanged()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override fun observePendingReceipts(): Flow<List<PendingReceipt>> {
        return pendingReceiptsDao.observePendingReceipts()
            .distinctUntilChanged()
            .map { it.map { k -> k.toDomain() } }
    }

    override fun observeAllParticipants(): Flow<List<String>> {
        return participantsDao.observeAllParticipants()
            .distinctUntilChanged()
            .map {
                it.map { p -> p.userId }
            }
    }

    override fun observeAllChats(): Flow<List<String>> {
        return chatDao.getAllChats()
            .distinctUntilChanged()
            .map {
                it.map { c -> c.chat.chatId }
            }
    }

    override suspend fun deletePendingMessage(pendingId: String) {
        database.safeExecute {
            pendingMessageDao.deletePendingMessage(pendingId)
        }
    }

    override suspend fun syncChats(chats: List<Chat>): AppResult<Unit, DataError.Local> {
        database.safeExecute {
            chats.forEach {
                val chatId = it.chatInfo.chatId

                chatDao.upsert(ChatEntity(chatId = chatId))

                when(val chatType = it.chatInfo.chatType) {
                    is ChatType.Direct -> {
                        participantsDao.upsert(
                            ChatParticipantEntity(
                                userId = chatType.me.userId,
                                username = chatType.me.username,
                                profilePictureUrl = chatType.me.profilePictureUrl
                            )
                        )

                        participantsDao.upsert(
                            ChatParticipantEntity(
                                userId = chatType.other.userId,
                                username = chatType.other.username,
                                profilePictureUrl = chatType.other.profilePictureUrl
                            )
                        )

                        participantsCrossRefDao.upsert(
                            ChatParticipantCrossRef(
                                chatId = chatId,
                                userId = chatType.me.userId
                            )
                        )

                        participantsCrossRefDao.upsert(
                            ChatParticipantCrossRef(
                                chatId = chatId,
                                userId = chatType.other.userId
                            )
                        )

                        directChatDao.upsert(
                            DirectChatEntity(
                                chatId = chatId,
                                meId = chatType.me.userId,
                                otherId = chatType.other.userId
                            )
                        )
                    }
                    is ChatType.Group -> {
                        chatType.participants.forEach {p ->
                            participantsDao.upsert(
                                ChatParticipantEntity(
                                    userId = p.userId,
                                    username = p.username,
                                    profilePictureUrl = p.profilePictureUrl
                                )
                            )

                            participantsCrossRefDao.upsert(
                                ChatParticipantCrossRef(
                                    chatId = chatId,
                                    userId = p.userId
                                )
                            )
                        }

                        groupChatDao.upsert(
                            GroupChatEntity(
                                chatId = chatId,
                                title = chatType.title,
                                pictureUrl = chatType.pictureUrl,
                                originator = chatType.originator.userId
                            )
                        )
                    }
                }

                it.chatMessages.forEach { msg ->
                    messageDao.upsert(
                        MessageEntity(
                            messageId = msg.messageId,
                            chatId = msg.chatId,
                            senderId = msg.owner.owner.userId,
                            message = (msg.content as OriginalMessage).toSerializable(),
                            timestamp = msg.timestamp.toEpochMilliseconds()
                        )
                    )

                    val status = StatusEntity(
                        statusId = msg.messageId, //Uuid.random().toString(),
                        messageId = msg.messageId
                    )

                    statusDao.upsert(status)

                    when(val owner = msg.owner) {
                        is MessageOwner.Me -> {
                            senderStatusDao.upsert(
                                SenderStatusEntity(
                                    statusId = status.statusId,
                                    status = owner.status.toSerializable()
                                )
                            )
                        }
                        is MessageOwner.Other -> {
                            receiverStatusDao.upsert(
                                ReceiverStatusEntity(
                                    statusId = status.statusId,
                                    status = owner.status.toSerializable()
                                )
                            )
                        }
                    }

                    if((msg.owner as? MessageOwner.Other)?.status == MessageStatus.ReceiverStatus.PENDING) {
                        pendingReceiptsDao.upsert(
                            PendingReceiptsEntity(
                                receipt = SerializableReceipt.MessageReceipt(
                                    message = msg.messageId,
                                    receipt = "DELIVERED"
                                )
                            )
                        )
                    }
                }
            }
        }

        return AppResult.Success(Unit)
    }

    override suspend fun updateStatusOfUpload(
        uploadId: String,
        status: UploadStatus
    ) {
        database.safeExecute {
            uploadablePendingMessageDao.updateStatus(uploadId, status.toSerializable())
        }
    }

    override suspend fun updateUploadablePendingMessageAsReady(pendingId: String, publicUrl: String) {
        database.safeExecute {
            updateStatusOfUpload(uploadId = pendingId, status = UploadStatus.Success(publicUrl))

            val pm = pendingMessageDao.getPendingMessageById(pendingId)!!
            val old = (pm.pending.message as SerializablePendingMessage.UploadablePendingMessage)

            val newOriginalMessage = when (val oldMessage = old.originalMessage) {
                is SerializableMessage.AudioMessage -> oldMessage.copy(audioUrl = publicUrl)
                is SerializableMessage.DocumentMessage -> oldMessage.copy(documentUrl = publicUrl)
                is SerializableMessage.ImageMessage -> oldMessage.copy(imageUrl = publicUrl)
                is SerializableMessage.VideoMessage -> oldMessage.copy(videoUrl = publicUrl)
                else -> TODO()
            }

            val newPendingMessage = pm.pending.copy(
                message = old.copy(
                    originalMessage = newOriginalMessage
                )
            )

            pendingMessageDao.upsert(newPendingMessage)

            pendingMessageDao.markPendingMessageAsReadyToSync(pendingId)
        }
    }

    override suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean) {
        database.safeExecute {
            onlineUsersDao.upsert(
                OnlineUsersEntity(
                    userId = userId,
                    isOnline = isOnline,
                    lastSeen = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
    }

    override suspend fun updatePendingReceiptAsCompleted(id: Long) {
        database.safeExecute {
            pendingReceiptsDao.updateReceiptAsComplete(id)
        }
    }

    override suspend fun markMessageAsRead(messageId: String) {
        database.safeExecute {
            pendingReceiptsDao.upsert(
                PendingReceiptsEntity(
                    receipt = SerializableReceipt.MessageReceipt(
                        message = messageId,
                        receipt = "READ"
                    )
                )
            )
        }
    }

    override suspend fun reset() {
        database.safeExecute {
            chatDao.deleteAll()
            participantsDao.deleteAll()
            pendingMessageDao.deleteAll()
            pendingReceiptsDao.deleteAll()
            messageDao.deleteAll()
            onlineUsersDao.deleteAll()
            directChatDao.deleteAll()
            groupChatDao.deleteAll()
            statusDao.deleteAll()
            senderStatusDao.deleteAll()
            receiverStatusDao.deleteAll()
            participantsCrossRefDao.deleteAll()
            uploadablePendingMessageDao.deleteAll()
        }
    }
}

private fun randomId(): String {
    val result = "abcdefghijklmnopqrstuvwxyz0123456789".repeat(10).toList().shuffled().take(30).joinToString("")
    return result
}
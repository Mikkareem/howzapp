package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.data.mappers.toDomain
import com.techullurgy.howzapp.feature.chat.data.mappers.toSerializable
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.entities.DirectChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.GroupChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.MessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingMessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatus
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatus
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.StatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.UploadablePendingMessageEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableMessage
import com.techullurgy.howzapp.feature.chat.database.models.SerializablePendingMessage
import com.techullurgy.howzapp.feature.chat.database.models.SerializableReceipt
import com.techullurgy.howzapp.feature.chat.database.utils.safeExecute
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatPreview
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single(binds = [ChatLocalRepository::class])
internal class DefaultChatLocalRepository(
    private val database: HowzappDatabase
): ChatLocalRepository {
    override suspend fun newPendingMessage(
        chatId: String,
        senderId: String,
        message: PendingMessage
    ): AppResult<Boolean, DataError.Local> {
        database.safeExecute {

            if (chatId.contains("__") && database.chatDao.getChatById(chatId).firstOrNull() == null) {
                database.chatDao.upsert(ChatEntity(chatId = chatId))

                database.directChatDao.upsert(
                    DirectChatEntity(
                        chatId = chatId,
                        meId = senderId,
                        otherId = chatId.split("__").first { it != senderId }
                    )
                )

                chatId.split("__").forEach {
                    database.participantsCrossRefDao.upsert(
                        ChatParticipantCrossRef(
                            chatId = chatId,
                            userId = it
                        )
                    )
                }
            }

            when(message) {
                is PendingMessage.NonUploadablePendingMessage -> {
                    database.pendingMessageDao.upsert(
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

                    database.pendingMessageDao.upsert(pendingMessage)

                    database.uploadablePendingMessageDao.upsert(
                        UploadablePendingMessageEntity(
                            pendingId = pendingMessage.pendingId,
                            uploadStatus = UploadStatus.Triggered().toSerializable()
                        )
                    )
                }
            }
        }

        return AppResult.Success(true)
    }

    override suspend fun messageSentAndDeletePendingMessage(pendingId: String, message: ChatMessage): AppResult<Boolean, DataError.Local> {
        database.safeExecute {
            database.messageDao.upsert(
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

            database.statusDao.upsert(status)

            database.senderStatusDao.upsert(
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
        return database.chatDao.getAllChats()
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
        return database.chatDao.getChatById(chatId)
            .distinctUntilChanged()
            .map {
                it?.toDomain()
            }
    }

    override fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatMessage>> {
        return database.pendingMessageDao.getPendingMessagesThatAreReadyToSync()
            .distinctUntilChanged()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override fun observeUploadableMessagesThatAreReadyToUpload(): Flow<List<ChatMessage>> {
        return database.pendingMessageDao.getUploadablePendingMessagesThatAreTriggered()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override fun observeUploadableMessagesThatAreCancelled(): Flow<List<ChatMessage>> {
        return database.pendingMessageDao.getUploadablePendingMessagesThatAreCancelled()
            .map {
                it.map { p -> p.toDomain() }
            }
    }

    override suspend fun deletePendingMessage(pendingId: String) {
        database.safeExecute {
            database.pendingMessageDao.deletePendingMessage(pendingId)
        }
    }

    override suspend fun syncChats(chats: List<Chat>): AppResult<Unit, DataError.Local> {
        database.safeExecute {
            chats.forEach {
                val chatId = it.chatInfo.chatId

                database.chatDao.upsert(ChatEntity(chatId = chatId))

                when(val chatType = it.chatInfo.chatType) {
                    is ChatType.Direct -> {
                        database.participantsDao.upsert(
                            ChatParticipantEntity(
                                userId = chatType.me.userId,
                                username = chatType.me.username,
                                profilePictureUrl = chatType.me.profilePictureUrl
                            )
                        )

                        database.participantsDao.upsert(
                            ChatParticipantEntity(
                                userId = chatType.other.userId,
                                username = chatType.other.username,
                                profilePictureUrl = chatType.other.profilePictureUrl
                            )
                        )

                        database.participantsCrossRefDao.upsert(
                            ChatParticipantCrossRef(
                                chatId = chatId,
                                userId = chatType.me.userId
                            )
                        )

                        database.participantsCrossRefDao.upsert(
                            ChatParticipantCrossRef(
                                chatId = chatId,
                                userId = chatType.other.userId
                            )
                        )

                        database.directChatDao.upsert(
                            DirectChatEntity(
                                chatId = chatId,
                                meId = chatType.me.userId,
                                otherId = chatType.other.userId
                            )
                        )
                    }
                    is ChatType.Group -> {
                        chatType.participants.forEach {p ->
                            database.participantsDao.upsert(
                                ChatParticipantEntity(
                                    userId = p.userId,
                                    username = p.username,
                                    profilePictureUrl = p.profilePictureUrl
                                )
                            )

                            database.participantsCrossRefDao.upsert(
                                ChatParticipantCrossRef(
                                    chatId = chatId,
                                    userId = p.userId
                                )
                            )
                        }

                        database.groupChatDao.upsert(
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
                    database.messageDao.upsert(
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

                    database.statusDao.upsert(status)

                    when(val owner = msg.owner) {
                        is MessageOwner.Me -> {
                            database.senderStatusDao.upsert(
                                SenderStatusEntity(
                                    statusId = status.statusId,
                                    status = SenderStatus.valueOf(owner.status.name)
                                )
                            )
                        }
                        is MessageOwner.Other -> {
                            database.receiverStatusDao.upsert(
                                ReceiverStatusEntity(
                                    statusId = status.statusId,
                                    status = ReceiverStatus.valueOf(owner.status.name)
                                )
                            )
                        }
                    }

                    database.pendingReceiptsDao.upsert(
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

        return AppResult.Success(Unit)
    }

    override suspend fun updateStatusOfUpload(
        uploadId: String,
        status: UploadStatus
    ) {
        database.safeExecute {
            database.uploadablePendingMessageDao.updateStatus(uploadId, status.toSerializable())
        }
    }

    override suspend fun updateUploadablePendingMessageAsReady(pendingId: String, publicUrl: String) {
        database.safeExecute {
            updateStatusOfUpload(uploadId = pendingId, status = UploadStatus.Success(publicUrl))

            val pm = database.pendingMessageDao.getPendingMessageById(pendingId)!!
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

            database.pendingMessageDao.upsert(newPendingMessage)

            database.pendingMessageDao.markPendingMessageAsReadyToSync(pendingId)
        }
    }
}

private fun randomId(): String {
    val result = "abcdefghijklmnopqrstuvwxyz0123456789".repeat(10).toList().shuffled().take(30).joinToString("")
    return result
}
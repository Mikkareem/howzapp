package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError
import com.techullurgy.howzapp.feature.chat.data.mappers.toSerializable
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import com.techullurgy.howzapp.feature.chat.database.entities.ChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantCrossRef
import com.techullurgy.howzapp.feature.chat.database.entities.ChatParticipantEntity
import com.techullurgy.howzapp.feature.chat.database.entities.DirectChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.GroupChatEntity
import com.techullurgy.howzapp.feature.chat.database.entities.MessageEntity
import com.techullurgy.howzapp.feature.chat.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatus
import com.techullurgy.howzapp.feature.chat.database.entities.ReceiverStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatus
import com.techullurgy.howzapp.feature.chat.database.entities.SenderStatusEntity
import com.techullurgy.howzapp.feature.chat.database.entities.StatusEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableReceipt
import com.techullurgy.howzapp.feature.chat.database.utils.safeExecute
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [ChatLocalRepository::class])
internal class DefaultChatLocalRepository(
    private val database: HowzappDatabase
): ChatLocalRepository {
    override suspend fun newPendingMessage(message: OriginalMessage): AppResult<Boolean, DataError.Local> {
        TODO("Not yet implemented")
    }

    override fun observeChatWithLastMessageAndUnreadCount(): Flow<List<Chat>> {
        TODO("Not yet implemented")
    }

    override fun observeConversation(chatId: String): Flow<Chat?> {
        TODO("Not yet implemented")
    }

    override fun observePendingMessagesThatAreReadyToSync(): Flow<List<ChatMessage>> {
        TODO("Not yet implemented")
    }

    override fun observeUploadableMessagesThatAreReadyToUpload(): Flow<List<ChatMessage>> {
        TODO("Not yet implemented")
    }

    override fun observeUploadableMessagesThatAreCancelled(): Flow<List<ChatMessage>> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}
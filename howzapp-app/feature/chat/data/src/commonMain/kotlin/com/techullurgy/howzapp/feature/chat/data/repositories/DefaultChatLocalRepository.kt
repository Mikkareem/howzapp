package com.techullurgy.howzapp.feature.chat.data.repositories

import com.techullurgy.howzapp.feature.chat.data.mappers.toChatMessage
import com.techullurgy.howzapp.feature.chat.data.mappers.toDomain
import com.techullurgy.howzapp.feature.chat.data.mappers.toEntity
import com.techullurgy.howzapp.feature.chat.data.mappers.toStatusEntity
import com.techullurgy.howzapp.feature.chat.database.HowzappDatabase
import com.techullurgy.howzapp.feature.chat.database.utils.safeExecute
import com.techullurgy.howzapp.feature.chat.domain.models.Chat
import com.techullurgy.howzapp.feature.chat.domain.models.ChatInfoWithLastMessage
import com.techullurgy.howzapp.feature.chat.domain.models.ChatMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.utils.isDeletable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [ChatLocalRepository::class])
internal class DefaultChatLocalRepository(
    private val database: HowzappDatabase
): ChatLocalRepository {
    override fun observeChatDetailsById(chatId: String): Flow<Chat?> {
        return database.chatDao.observeChatDetailsById(chatId).map { it?.toDomain() }
    }

    override fun observeChatInfoWithLastMessage(): Flow<List<ChatInfoWithLastMessage>> {
        return database.chatDao.observeChatInfos().map { t -> t.map { it.toDomain() } }
    }

    override suspend fun syncChats(chats: List<Chat>) {
        database.safeExecute {
            chats.forEach { chat ->
                val oldChat = database.chatDao.getChatDetailsById(chat.chatInfo.chatId)

                oldChat?.messages?.filter {
                    !it.toChatMessage().isDeletable()
                }?.forEach {
                    database.chatDao.deleteMessagesById(it.message.messageId)
                }

                oldChat?.participants?.forEach {
                    database.chatDao.deleteParticipantsFromChat(chat.chatInfo.chatId, it.userId)
                }

                if(oldChat == null) {
                    if(database.chatDao.getParticipantById(chat.chatInfo.originator.userId) == null) {
                        database.chatDao.upsertParticipant(chat.chatInfo.originator.toEntity())
                    }
                    database.chatDao.upsertChat(chat.chatInfo.toEntity())
                }

                chat.chatParticipants.map { it.toEntity() }.forEach {
                    database.chatDao.upsertParticipantToTheChat(chat.chatInfo.chatId, it)
                }

                chat.chatMessages.forEach {
                    updateMessage(it)
                }
            }
        }
    }

    override suspend fun updateMessage(message: ChatMessage) {
        database.safeExecute {
            val messageEntity = message.toEntity()
            database.chatDao.upsertMessage(messageEntity)

            val statusEntity = message.toStatusEntity()
            statusEntity?.let {
                database.chatDao.upsertMessageStatus(it)
            }
        }
    }

    override suspend fun updateMessageAsSync(oldMessageId: String, message: ChatMessage) {
        database.safeExecute {
            database.chatDao.updateChatMessageAsSync(
                oldMessageId,
                message.messageId,
                message.timestamp.toEpochMilliseconds()
            )
            updateMessage(message)
        }
    }
}
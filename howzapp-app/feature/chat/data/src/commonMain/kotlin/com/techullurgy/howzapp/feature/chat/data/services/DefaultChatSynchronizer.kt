package com.techullurgy.howzapp.feature.chat.data.services

import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.chat.data.mappers.toChatMessage
import com.techullurgy.howzapp.feature.chat.database.dao.ChatDao
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.feature.chat.domain.services.ChatSynchronizer
import com.techullurgy.howzapp.feature.chat.domain.utils.markAsReadyToSync
import com.techullurgy.howzapp.feature.chat.domain.utils.upgradeToOriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.utils.upgradeToSuccessUrl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

internal class DefaultChatSynchronizer(
    private val chatLocalRepository: ChatLocalRepository,
    private val chatNetworkRepository: ChatNetworkRepository,
    chatDao: ChatDao,
): ChatSynchronizer {
    private val syncTriggeredChannel = Channel<Unit>()

    private val chatsSyncFlow = syncTriggeredChannel.consumeAsFlow()
        .debounce(3000)
        .onEach {
            chatNetworkRepository.syncChats()
                .onSuccess {
                    chatLocalRepository.syncChats(it)
                }
        }


    private val pendingMessagesSync: Flow<Unit> =
        chatDao.observePendingMessagesThatAreReadyToSync()
            .distinctUntilChanged()
            .onEach { messages ->
                messages.forEach { pendingMessage ->
                    val sendableMessage = pendingMessage.message.toChatMessage().upgradeToOriginalMessage()
                    chatNetworkRepository.sendMessage(sendableMessage)
                        .onSuccess { updatedMessage ->
                            chatLocalRepository.updateMessageAsSync(sendableMessage.messageId, updatedMessage)
                        }
                }
            }
            .map { }

    private val pendingUploadsTriggeredSync: Flow<Unit> =
        chatDao.observeUploadablePendingMessageTriggered()
            .onEach {
                it.map { uploadMessage ->
                    val message = uploadMessage.uploads.toChatMessage()
                    chatNetworkRepository.uploadMessage(
                        message = message
                    ) { publicUrl ->
                        val upgradedMessage = message.upgradeToSuccessUrl(publicUrl)
                        chatLocalRepository.updateMessage(upgradedMessage)
                    }
                }
            }
            .map {  }

    private val pendingUploadsSuccessSync: Flow<Unit> =
        chatDao.observeUploadablePendingMessageCompletedSuccess()
            .onEach {
                it.map { uploadedMessage ->
                    val originalMessage = uploadedMessage.uploads.toChatMessage()
                        .markAsReadyToSync()
                    chatLocalRepository.updateMessage(originalMessage)
                }
            }
            .map {  }


    override val syncsFlow: Flow<Unit> = merge(
        pendingUploadsTriggeredSync,
        pendingUploadsSuccessSync,
        pendingMessagesSync,
        chatsSyncFlow
    )

    override fun triggerSync() {
        syncTriggeredChannel.trySend(Unit)
    }
}
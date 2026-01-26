package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.common.utils.dropDuplicates
import com.techullurgy.howzapp.common.utils.flatten
import com.techullurgy.howzapp.common.utils.models.onSuccess
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.features.chats.models.MessageOwner
import com.techullurgy.howzapp.features.chats.models.MessageStatus
import com.techullurgy.howzapp.features.chats.models.PendingMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class PendingMessageSenderService(
    appScope: CoroutineScope,
    chatLocalRepository: ChatLocalRepository,
    chatNetworkRepository: ChatNetworkRepository,
) {
    init {
        chatLocalRepository.observePendingMessagesThatAreReadyToSync()
            .flatten()
            .dropDuplicates()
            .onEach { pendingMessage ->
                val message = pendingMessage.copy(content = (pendingMessage.content as PendingMessage).originalMessage)
                chatNetworkRepository.sendMessage(message)
                    .onSuccess { messageId ->
                        val newMessage = message.copy(
                            messageId = messageId,
                            owner = (message.owner as MessageOwner.Me).copy(status = MessageStatus.SenderStatus.SENT)
                        )
                        chatLocalRepository.messageSentAndDeletePendingMessage(pendingMessage.messageId, newMessage)
                    }
            }
            .launchIn(appScope)
    }
}
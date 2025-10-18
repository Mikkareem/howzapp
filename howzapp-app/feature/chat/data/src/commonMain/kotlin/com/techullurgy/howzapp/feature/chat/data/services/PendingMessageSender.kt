package com.techullurgy.howzapp.feature.chat.data.services

import com.techullurgy.howzapp.core.domain.util.dropDuplicates
import com.techullurgy.howzapp.core.domain.util.flatten
import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PendingMessageSender(
    appScope: CoroutineScope,
    chatLocalRepository: ChatLocalRepository,
    chatNetworkRepository: ChatNetworkRepository,
) {
    init {
        chatLocalRepository.observePendingMessagesThatAreReadyToSync()
            .flatten()
            .dropDuplicates()
            .onEach {  pendingMessage ->
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
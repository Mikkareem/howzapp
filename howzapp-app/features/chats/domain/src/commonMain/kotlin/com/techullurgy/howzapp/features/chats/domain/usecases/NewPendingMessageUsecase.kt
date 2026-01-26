package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.core.session.SessionNotifier
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.models.OriginalMessage
import com.techullurgy.howzapp.features.chats.models.PendingMessage
import com.techullurgy.howzapp.features.chats.models.UploadStatus
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory

@Factory
class NewPendingMessageUsecase(
    private val chatLocalRepository: ChatLocalRepository,
    private val sessionNotifier: SessionNotifier,
) {
    suspend operator fun invoke(
        chatId: String,
        message: OriginalMessage
    ) {
        val pendingMessage = when (message) {
            is OriginalMessage.UploadableMessage -> {
                PendingMessage.UploadablePendingMessage(
                    status = UploadStatus.Triggered(),
                    originalMessage = message
                )
            }

            is OriginalMessage.NonUploadableMessage -> {
                PendingMessage.NonUploadablePendingMessage(
                    originalMessage = message
                )
            }
        }

        val senderId = sessionNotifier.observeSessionInfo().firstOrNull()?.id ?: return

        chatLocalRepository.newPendingMessage(chatId, senderId, pendingMessage)
    }
}
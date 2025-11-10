package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.core.domain.auth.SessionStorage
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.PendingMessage
import com.techullurgy.howzapp.feature.chat.domain.models.UploadStatus
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory

@Factory
class NewPendingMessageUsecase(
    private val chatLocalRepository: ChatLocalRepository,
    private val sessionStorage: SessionStorage,
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

        val senderId = sessionStorage.observeAuthInfo().firstOrNull()?.id ?: return

        chatLocalRepository.newPendingMessage(chatId, senderId, pendingMessage)
    }
}
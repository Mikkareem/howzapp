package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import org.koin.core.annotation.Single

@Single
class RecordingAudioNotifierUsecase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String) {
        chatRepository.recordingAudioFor(chatId)
    }
}
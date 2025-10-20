package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEventType
import com.techullurgy.howzapp.feature.chat.domain.networking.WebsocketConnector
import com.techullurgy.howzapp.feature.chat.domain.networking.events.IncomingMessage
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class UserChatEventsTracker(
    connector: WebsocketConnector,
    appScope: CoroutineScope,
    repository: ChatRepository
) {
    init {
        merge(
            connector.messages.filterIsInstance<IncomingMessage.TypingMessage>(),
            connector.messages.filterIsInstance<IncomingMessage.RecordingAudioMessage>()
        )
            .onEach {
                when(it) {
                    is IncomingMessage.RecordingAudioMessage -> {
                        repository.newEvent(
                            UserChatEvent(
                                chatId = it.chatId,
                                userId = it.userId,
                                eventType = UserChatEventType.RECORDING_AUDIO
                            )
                        )
                    }
                    is IncomingMessage.TypingMessage -> {
                        repository.newEvent(
                            UserChatEvent(
                                chatId = it.chatId,
                                userId = it.userId,
                                eventType = UserChatEventType.TYPING
                            )
                        )
                    }
                    else -> TODO()
                }
            }
            .launchIn(appScope)
    }
}
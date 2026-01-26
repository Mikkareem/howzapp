package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.services.UserChatEventsTrackerService
import com.techullurgy.howzapp.features.chats.models.UserChatEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.koin.core.annotation.Factory

@Factory
class GetUserChatEventsByChatIdUsecase internal constructor(
    private val userEventsTracker: UserChatEventsTrackerService
) {
    operator fun invoke(
        chatId: String
    ): Flow<UserChatEvent?> {
        return userEventsTracker.events
            .transform { events ->
                val found = events.firstOrNull { it.chatId == chatId }
                emit(found)
            }
    }
}
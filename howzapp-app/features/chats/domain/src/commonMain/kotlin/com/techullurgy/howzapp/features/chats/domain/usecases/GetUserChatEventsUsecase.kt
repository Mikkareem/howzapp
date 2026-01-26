package com.techullurgy.howzapp.features.chats.domain.usecases

import com.techullurgy.howzapp.features.chats.domain.services.UserChatEventsTrackerService
import com.techullurgy.howzapp.features.chats.models.UserChatEvent
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetUserChatEventsUsecase internal constructor(
    private val userEventsTracker: UserChatEventsTrackerService
) {
    operator fun invoke(): Flow<List<UserChatEvent>> {
        return userEventsTracker.events
    }
}
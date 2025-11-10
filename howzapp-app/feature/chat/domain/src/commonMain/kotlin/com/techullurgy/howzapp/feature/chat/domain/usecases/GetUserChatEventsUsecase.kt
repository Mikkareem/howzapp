package com.techullurgy.howzapp.feature.chat.domain.usecases

import com.techullurgy.howzapp.feature.chat.domain.models.UserChatEvent
import com.techullurgy.howzapp.feature.chat.domain.services.UserChatEventsTrackerService
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
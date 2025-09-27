package com.techullurgy.howzapp.events.services

import com.techullurgy.howzapp.common.events.ServerReceipt
import com.techullurgy.howzapp.common.types.EventSourceId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.events.infra.database.repositories.EventSourceRepository
import org.springframework.stereotype.Service

@Service
class EventSourceService(
    private val eventSourceRepository: EventSourceRepository
) {
    fun getEventsFor(user: UserId): List<ServerReceipt> {
        return eventSourceRepository.getEventsFor(user)
    }

    fun consumeEvent(event: EventSourceId) {
        return eventSourceRepository.update(
            eventSourceRepository.findById(event).get()
                .copy(consumed = true)
        )
    }
}
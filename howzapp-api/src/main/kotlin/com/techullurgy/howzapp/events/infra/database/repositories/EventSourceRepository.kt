package com.techullurgy.howzapp.events.infra.database.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.techullurgy.howzapp.common.events.ServerAppEvent
import com.techullurgy.howzapp.common.events.ServerReceipt
import com.techullurgy.howzapp.common.types.EventSourceId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.events.infra.database.entities.EventSourceEntity
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaEventSourceRepository: JpaRepository<EventSourceEntity, EventSourceId> {
    fun findByEventFor(user: UserId): List<EventSourceEntity>
}

@Repository
class EventSourceRepository(
    private val objectMapper: ObjectMapper,
    private val appEventPublisher: ApplicationEventPublisher,
    private val eventSourceRepository: JpaEventSourceRepository
) {

    fun findById(id: EventSourceId): Optional<EventSourceEntity> {
        return eventSourceRepository.findById(id)
    }

    fun update(updated: EventSourceEntity) {
        eventSourceRepository.save(updated)
    }

    fun insert(entity: EventSourceEntity) {
        eventSourceRepository.save(entity)
        appEventPublisher.publishEvent(entity)
    }

    fun getEventsFor(user: UserId): List<ServerReceipt> {
        return eventSourceRepository.findByEventFor(user).map {
            ServerReceipt(
                id = it.id,
                receiptFor = it.eventFor,
                event = objectMapper.readValue(it.payload, ServerAppEvent::class.java),
            )
        }
    }
}
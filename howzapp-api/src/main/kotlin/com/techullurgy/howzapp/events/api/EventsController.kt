package com.techullurgy.howzapp.events.api

import com.techullurgy.howzapp.common.events.ServerReceipt
import com.techullurgy.howzapp.common.types.EventSourceId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.events.services.EventSourceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventsController(
    private val eventSourceService: EventSourceService
) {
    @GetMapping
    fun getEventsFor(@RequestParam("userId") userId: String): ResponseEntity<List<ServerReceipt>> {
        val user = UserId.parse(userId)
        return ResponseEntity.ok(eventSourceService.getEventsFor(user))
    }

    @PutMapping
    fun consumeEvent(@RequestParam("eventId") eventId: String): ResponseEntity<Unit> {
        val event = EventSourceId.parse(eventId)
        eventSourceService.consumeEvent(event)
        return ResponseEntity.ok(Unit)
    }
}
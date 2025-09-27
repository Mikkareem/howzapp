package com.techullurgy.howzapp.notifications.infra.networks

import com.techullurgy.howzapp.common.events.ServerReceipt
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "events-client", url = $$"${application.services.events.url}")
interface EventsClient {

    @GetMapping
    fun receiptEventsFor(@RequestParam("userId") userId: String): List<ServerReceipt>

    @PutMapping
    fun consumeEvent(@RequestParam("eventId") eventId: String)
}
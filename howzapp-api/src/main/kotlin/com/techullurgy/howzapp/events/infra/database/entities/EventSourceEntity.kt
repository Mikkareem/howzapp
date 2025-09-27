package com.techullurgy.howzapp.events.infra.database.entities

import com.fasterxml.jackson.databind.ObjectMapper
import com.techullurgy.howzapp.common.events.ServerReceipt
import com.techullurgy.howzapp.common.types.EventSourceId
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.common.types.id
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
data class EventSourceEntity(
    val eventFor: UserId,
    val payload: String,
    val consumed: Boolean = false
) {
    @Id val id: EventSourceId = EventSourceId.id

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: Instant = Instant.now()
}

context(mapper: ObjectMapper)
fun ServerReceipt.toEntity() = EventSourceEntity(
    receiptFor,
    mapper.writeValueAsString(event)
)
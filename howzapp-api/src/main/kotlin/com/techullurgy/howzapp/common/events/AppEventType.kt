package com.techullurgy.howzapp.common.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.techullurgy.howzapp.common.types.UserId

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(ServerAppEvent.NotifySyncMessageEvent::class, "notify_sync"),
)
sealed interface ServerAppEvent {
    data class NotifySyncMessageEvent(val targetUser: UserId) : ServerAppEvent
}
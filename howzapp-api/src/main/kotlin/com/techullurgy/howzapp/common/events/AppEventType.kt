package com.techullurgy.howzapp.common.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(ServerAppEvent.ChatNewMessageEvent::class, "new_message"),
    JsonSubTypes.Type(ServerAppEvent.ChatMessageUpdatedEvent::class, "message_updated")
)
sealed interface ServerAppEvent {
    // 1) When new message is received for client
    data class ChatNewMessageEvent(val chatId: String): ServerAppEvent
    // 2) When message status is updated for client (sent, received, read)
    data class ChatMessageUpdatedEvent(val messageId: String): ServerAppEvent
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(ClientAppEvent.ChatMessageReadEvent::class, "message_read"),
)
sealed interface ClientAppEvent {
    // 1) When the user is read the message
    data class ChatMessageReadEvent(val messageId: String): ClientAppEvent
    // 2) When the user is deleted the message
}
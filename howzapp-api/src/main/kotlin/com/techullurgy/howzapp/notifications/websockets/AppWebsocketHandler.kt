package com.techullurgy.howzapp.notifications.websockets

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.techullurgy.howzapp.common.events.ServerAppEvent
import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


@Component
class AppWebsocketHandler(
    private val mapper: ObjectMapper
): TextWebSocketHandler() {
    private val lock = ReentrantReadWriteLock()

    private val sessions = ConcurrentHashMap<UserId, WebsocketUserSession>()

    private val chatSubscribers = ConcurrentHashMap<ChatId, MutableSet<UserId>>()
    private val participantSubscribers = ConcurrentHashMap<UserId, MutableSet<UserId>>()

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        super.afterConnectionClosed(session, status)

        val currentUser = session.principal?.name ?: run {
            session.close()
            return
        }

        lock.write {
            sessions.remove(currentUser)
            participantSubscribers.remove(currentUser)

            chatSubscribers.forEach {
                it.value.remove(currentUser)
            }

            chatSubscribers.filter { it.value.isEmpty() }.map { it.key }.also {
                it.forEach { chat ->
                    chatSubscribers.remove(chat)
                }
            }
        }
    }

    override fun handleTransportError(
        session: WebSocketSession,
        exception: Throwable
    ) {
        super.handleTransportError(session, exception)
    }

    override fun handlePongMessage(
        session: WebSocketSession,
        message: PongMessage
    ) {
        super.handlePongMessage(session, message)
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        super.handleTextMessage(session, message)

        val from = session.principal?.name ?: run {
            session.close()
            return
        }

        when (val incomingMessage = mapper.readValue<IncomingMessage>(message.payload)) {
            is IncomingMessage.ParticipantSubscriptionMessage -> {
                handleParticipantSubscription(from, incomingMessage)
            }

            is IncomingMessage.RecordingAudioMessage -> {
                handleRecordingAudioMessage(from, incomingMessage)
            }

            is IncomingMessage.TypingMessage -> {
                handleTypingMessage(from, incomingMessage)
            }
        }
    }

    fun handleNotifySyncMessageEvent(event: ServerAppEvent.NotifySyncMessageEvent) {
        with(mapper) {
            lock.read { sessions[event.targetUser] }?.sendMessage(
                OutgoingMessage.NotifyMessageSyncMessage
            )?.let {
                if (!it) {
                    println("Sending via Firebase Push Notifications")
                }
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)

        val userId = session.principal?.name ?: run {
            session.close()
            return
        }

        val userSession = WebsocketUserSession(
            userId = userId,
            session = ConcurrentWebSocketSessionDecorator(session, 10_000, 20_000),
        )

        lock.write { sessions[userId] = userSession }

        // Fetch for chatIds for this user and populate chatSubscribers
        fetchAndPopulateChats(userId)
    }

    private fun handleParticipantSubscription(from: UserId, message: IncomingMessage.ParticipantSubscriptionMessage) {
        val participants = message.participants

        lock.write {
            participantSubscribers.getOrPut(from, defaultValue = { mutableSetOf() }).addAll(participants)
        }
    }

    private fun handleRecordingAudioMessage(from: UserId, message: IncomingMessage.RecordingAudioMessage) {
        val desiredChat = message.chatId

        val outgoingMessage: OutgoingMessage = OutgoingMessage.RecordingAudioMessage(
            chatId = desiredChat,
            userId = from
        )

        val currentSubscribers = lock.read { chatSubscribers[desiredChat]!! }
        val currentSessions = lock.read { sessions }

        currentSubscribers.filter { it != from }.forEach {
            with(mapper) {
                currentSessions[it]?.sendMessage(outgoingMessage)
            }
        }
    }

    private fun handleTypingMessage(from: UserId, message: IncomingMessage.TypingMessage) {

        val desiredChat = message.chatId

        val outgoingMessage: OutgoingMessage = OutgoingMessage.TypingMessage(
            chatId = desiredChat,
            userId = from
        )

        val currentSubscribers = lock.read { chatSubscribers[desiredChat]!! }
        val currentSessions = lock.read { sessions }

        currentSubscribers.filter { it != from }.forEach {
            with(mapper) {
                currentSessions[it]?.sendMessage(outgoingMessage)
            }
        }
    }

    private fun fetchAndPopulateChats(userId: UserId) {
        val chatIds = listOf<ChatId>() // Network Result
        val participantIds = listOf<UserId>() // Network Result

        lock.write {
            chatIds.forEach {
                chatSubscribers.getOrPut(key = it, defaultValue = { mutableSetOf() }).add(userId)
            }
            participantSubscribers.getOrPut(key = userId, defaultValue = { mutableSetOf() }).addAll(participantIds)
        }
    }
}

private data class WebsocketUserSession(
    val userId: UserId,
    val session: WebSocketSession,
    val lastPongTimestamp: Instant = Instant.now()
) {
    context(mapper: ObjectMapper)
    fun sendMessage(message: OutgoingMessage): Boolean {
        if (session.isOpen) {
            session.sendMessage(TextMessage(mapper.writeValueAsString(message)))
            return true
        }

        return false
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(IncomingMessage.TypingMessage::class, name = "typing"),
    JsonSubTypes.Type(IncomingMessage.RecordingAudioMessage::class, name = "recording_audio"),
    JsonSubTypes.Type(IncomingMessage.ParticipantSubscriptionMessage::class, name = "participant_subscription"),
)
private sealed interface IncomingMessage {
    data class TypingMessage(val chatId: ChatId) : IncomingMessage
    data class RecordingAudioMessage(val chatId: ChatId) : IncomingMessage
    data class ParticipantSubscriptionMessage(val participants: List<UserId>) : IncomingMessage
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(OutgoingMessage.TypingMessage::class, name = "typing"),
    JsonSubTypes.Type(OutgoingMessage.RecordingAudioMessage::class, name = "recording_audio"),
    JsonSubTypes.Type(OutgoingMessage.OnlineIndicatorMessage::class, name = "online_indicator"),
    JsonSubTypes.Type(OutgoingMessage.OfflineIndicatorMessage::class, name = "offline_indicator"),
    JsonSubTypes.Type(OutgoingMessage.NotifyMessageSyncMessage::class, name = "message_sync"),
)
private sealed interface OutgoingMessage {
    data class TypingMessage(val chatId: ChatId, val userId: UserId) : OutgoingMessage
    data class RecordingAudioMessage(val chatId: ChatId, val userId: UserId) : OutgoingMessage
    data class OnlineIndicatorMessage(val userId: UserId) : OutgoingMessage
    data class OfflineIndicatorMessage(val userId: UserId) : OutgoingMessage
    data object NotifyMessageSyncMessage : OutgoingMessage
}
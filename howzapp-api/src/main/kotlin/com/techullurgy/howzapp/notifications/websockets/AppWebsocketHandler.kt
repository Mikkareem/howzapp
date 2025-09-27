package com.techullurgy.howzapp.notifications.websockets

import com.fasterxml.jackson.databind.ObjectMapper
import com.techullurgy.howzapp.amqp.RabbitMQConfiguration
import com.techullurgy.howzapp.common.events.ClientAppEvent
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.notifications.infra.networks.EventsClient
import com.techullurgy.howzapp.notifications.websockets.models.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write
import kotlin.uuid.Uuid


@Component
class AppWebsocketHandler(
    private val objectMapper: ObjectMapper,
    private val eventsClient: EventsClient,
    private val rabbitTemplate: RabbitTemplate
): TextWebSocketHandler() {

    private val connectionLock = ReentrantReadWriteLock()

    private val sessions = ConcurrentHashMap<String, WebsocketUserSession>()
    private val userSessions = ConcurrentHashMap<Uuid, String>()

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)

        connectionLock.write {
            sessions.remove(session.id)?.let {
                userSessions.remove(it.userId)
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)

        val userId = (session.handshakeHeaders.getFirst("x-user"))
            ?.run { UserId.parse(this) }
            ?: let {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication Failed"))
                return
            }

        val safeSession = ConcurrentWebSocketSessionDecorator(
            session,
            1000,
            1024 * 1024
        )

        connectionLock.write {
            sessions[session.id] = WebsocketUserSession(userId, safeSession)
            userSessions[userId] = session.id
        }

        eventsClient.receiptEventsFor(userId.toString()).toSet()
            .takeIf { it.isNotEmpty() }
            ?.let {
                it.forEach { event ->
                    val outgoingMessage = OutgoingMessage(
                        type = OutgoingMessageType.SYNC_REQUEST,
                        payload = event.toString()
                    )

                    if(session.isOpen) {
                        session.sendMessage(TextMessage(objectMapper.writeValueAsString(outgoingMessage)))
                    }
                }
            }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        super.handleTextMessage(session, message)

        val incomingMessage = objectMapper.readValue(message.payload, IncomingMessage::class.java)

        when (incomingMessage.type) {
            IncomingMessageType.TYPING -> {}
            IncomingMessageType.TYPING_DONE -> {}
            IncomingMessageType.SYNC -> {
                handleClientSync(incomingMessage.payload)
            }
        }
    }

    private fun handleClientSync(payload: String) {
        val clientEvent = objectMapper.readValue(payload, ClientAppEvent::class.java)
        rabbitTemplate.convertAndSend(
            RabbitMQConfiguration.RECEIPTS__ON_NEW_RECEIPT,
            "",
            clientEvent
        )
    }
}

private data class WebsocketUserSession(
    val userId: UserId,
    val session: WebSocketSession,
    val lastPongTimestamp: Instant = Instant.now()
)
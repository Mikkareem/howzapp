package com.techullurgy.howzapp.chats.services

import com.techullurgy.howzapp.amqp.RabbitMQConfiguration
import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.common.events.ServerAppEvent.NotifySyncMessageEvent
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ChatEventHandler(
    private val rabbitTemplate: RabbitTemplate,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleChatEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnNewMessage -> {
                rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.APP_RECEIPT_EXCHANGE,
                    "",
                    NotifySyncMessageEvent(event.toId)
                )
            }

            is ChatEvent.OnDeliveredMessage -> {
                rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.APP_RECEIPT_EXCHANGE,
                    "",
                    NotifySyncMessageEvent(event.toId)
                )
            }

            is ChatEvent.OnReadMessage -> {
                rabbitTemplate.convertAndSend(
                    RabbitMQConfiguration.APP_RECEIPT_EXCHANGE,
                    "",
                    NotifySyncMessageEvent(event.toId)
                )
            }
        }
    }
}
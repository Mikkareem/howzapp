package com.techullurgy.howzapp.chats.services

import com.techullurgy.howzapp.amqp.RabbitMQConfiguration
import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.common.events.ServerAppEvent
import com.techullurgy.howzapp.common.events.ServerReceipt
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ChatEventHandler(
    private val rabbitTemplate: RabbitTemplate,
    private val chatService: ChatService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleChatEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnNewMessage -> {
                chatService.getNotifiableParticipantsForChat(event.chatId, event.fromId).forEach {
                    rabbitTemplate.convertAndSend(
                        RabbitMQConfiguration.APP_RECEIPT_EXCHANGE,
                        "",
                        ServerReceipt(
                            receiptFor = it.id,
                            event = ServerAppEvent.ChatNewMessageEvent(event.chatId.toString()),
                        )
                    )
                }
            }
        }
    }
}
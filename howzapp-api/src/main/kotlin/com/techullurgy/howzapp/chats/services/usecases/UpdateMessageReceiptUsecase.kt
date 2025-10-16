package com.techullurgy.howzapp.chats.services.usecases

import com.techullurgy.howzapp.chats.events.ChatEvent
import com.techullurgy.howzapp.chats.infra.database.entities.MessageStatus
import com.techullurgy.howzapp.chats.infra.database.entities.Receipt
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageReceiptsRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageRepository
import com.techullurgy.howzapp.chats.infra.database.repositories.ChatMessageStatusRepository
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class UpdateMessageReceiptUsecase(
    private val receiptsRepository: ChatMessageReceiptsRepository,
    private val statusRepository: ChatMessageStatusRepository,
    private val messageRepository: ChatMessageRepository,
    private val appEventPublisher: ApplicationEventPublisher
) {

    operator fun invoke(
        participantId: UserId,
        messageId: MessageId,
        receipt: Receipt
    ) {
        receiptsRepository.updateReceiptFor(participantId, messageId, receipt)

        val senderId = messageRepository.findById(messageId).get().sender.id

        when (receipt) {
            Receipt.DELIVERED -> {
                if (receiptsRepository.hasAllAtLeastReceiptForMessage(messageId, Receipt.DELIVERED)) {
                    statusRepository.updateStatusForTheMessage(messageId, MessageStatus.RECEIVED)
                    appEventPublisher.publishEvent(
                        ChatEvent.OnDeliveredMessage(senderId)
                    )
                }
            }

            Receipt.READ -> {
                if (receiptsRepository.hasAllAtLeastReceiptForMessage(messageId, Receipt.READ)) {
                    statusRepository.updateStatusForTheMessage(messageId, MessageStatus.READ)
                    appEventPublisher.publishEvent(
                        ChatEvent.OnReadMessage(senderId)
                    )
                }
            }

            else -> {}
        }
    }
}
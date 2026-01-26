package com.techullurgy.howzapp.features.chats.domain.services

import com.techullurgy.howzapp.common.utils.dropDuplicates
import com.techullurgy.howzapp.common.utils.models.onSuccess
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.features.chats.domain.repositories.ChatNetworkRepository
import com.techullurgy.howzapp.features.chats.models.Receipt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
internal class PendingReceiptsSenderService(
    chatLocalRepository: ChatLocalRepository,
    chatNetworkRepository: ChatNetworkRepository,
    applicationScope: CoroutineScope
) {
    init {
        chatLocalRepository.observePendingReceipts()
            .dropDuplicates()
            .onEach { receipts ->
                supervisorScope {
                    receipts.map { receipt ->
                        async {
                            when(val type = receipt.receipt) {
                                is Receipt.MessageReceipt -> {
                                    when(type.receipt) {
                                        "DELIVERED" -> {
                                            chatNetworkRepository.sendDeliveryReceiptToMessage(type.message)
                                                .onSuccess {
                                                    chatLocalRepository.updatePendingReceiptAsCompleted(receipt.id)
                                                }
                                        }
                                        "READ" -> {
                                            chatNetworkRepository.sendReadReceiptToMessage(type.message)
                                                .onSuccess {
                                                    chatLocalRepository.updatePendingReceiptAsCompleted(receipt.id)
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .launchIn(applicationScope)
    }
}
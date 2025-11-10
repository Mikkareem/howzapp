package com.techullurgy.howzapp.feature.chat.domain.services

import com.techullurgy.howzapp.core.domain.util.dropDuplicates
import com.techullurgy.howzapp.core.domain.util.onSuccess
import com.techullurgy.howzapp.feature.chat.domain.models.Receipt
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatLocalRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PendingReceiptsSenderService(
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
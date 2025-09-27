package com.techullurgy.howzapp.common.events
import com.techullurgy.howzapp.common.types.UserId
import kotlin.uuid.Uuid

data class ServerReceipt(
    val id: Uuid? = null,
    val receiptFor: UserId,
    val event: ServerAppEvent,
)

data class ClientReceipt(
    val receiptFrom: UserId,
    val event: ClientAppEvent,
)
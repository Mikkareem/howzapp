package com.techullurgy.howzapp.features.chats.data.mappers

import com.techullurgy.howzapp.features.chats.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.features.chats.database.models.SerializableReceipt
import com.techullurgy.howzapp.features.chats.models.PendingReceipt
import com.techullurgy.howzapp.features.chats.models.Receipt

fun PendingReceiptsEntity.toDomain(): PendingReceipt {
    return PendingReceipt(
        id = id,
        receipt = receipt.toDomain()
    )
}

fun SerializableReceipt.toDomain(): Receipt {
    return when (this) {
        is SerializableReceipt.MessageReceipt -> Receipt.MessageReceipt(message, receipt)
    }
}
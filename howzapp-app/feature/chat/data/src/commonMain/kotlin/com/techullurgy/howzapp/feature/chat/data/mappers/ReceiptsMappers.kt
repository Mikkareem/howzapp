package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.entities.PendingReceiptsEntity
import com.techullurgy.howzapp.feature.chat.database.models.SerializableReceipt
import com.techullurgy.howzapp.feature.chat.domain.models.PendingReceipt
import com.techullurgy.howzapp.feature.chat.domain.models.Receipt

fun PendingReceiptsEntity.toDomain(): PendingReceipt {
    return PendingReceipt(
        id = id,
        receipt = receipt.toDomain()
    )
}

fun SerializableReceipt.toDomain(): Receipt {
    return when(this) {
        is SerializableReceipt.MessageReceipt -> Receipt.MessageReceipt(message, receipt)
    }
}
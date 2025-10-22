package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface Receipt {
    data class MessageReceipt(
        val message: String,
        val receipt: String
    ): Receipt
}
package com.techullurgy.howzapp.features.chats.models

sealed interface Receipt {
    data class MessageReceipt(
        val message: String,
        val receipt: String
    ): Receipt
}
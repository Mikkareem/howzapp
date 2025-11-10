package com.techullurgy.howzapp.feature.chat.database.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface SerializableReceipt {

    @Serializable
    data class MessageReceipt(
        val message: String,
        val receipt: String,
    ): SerializableReceipt
}
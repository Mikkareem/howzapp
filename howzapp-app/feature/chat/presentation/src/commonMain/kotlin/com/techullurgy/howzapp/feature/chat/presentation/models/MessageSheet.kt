package com.techullurgy.howzapp.feature.chat.presentation.models

import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.OriginalMessage
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import kotlin.time.Instant

internal data class MessageSheet(
    val messageId: String,
    val sender: ChatParticipant,
    val isPictureShowable: Boolean,
    val message: OriginalMessage,
    val messageOwner: MessageOwner,
    val timestamp: Instant
) {
    val isCurrentUser = messageOwner is MessageOwner.Me
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageSheet) return false

        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }
}
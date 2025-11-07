package com.techullurgy.howzapp.feature.chat.presentation.models

import com.techullurgy.howzapp.feature.chat.domain.models.ChatParticipant
import com.techullurgy.howzapp.feature.chat.domain.models.Message
import com.techullurgy.howzapp.feature.chat.domain.models.MessageOwner
import com.techullurgy.howzapp.feature.chat.domain.models.MessageStatus
import kotlin.time.Instant

internal data class MessageSheet(
    val messageId: String,
    val sender: ChatParticipant,
    val isPictureShowable: Boolean,
    val message: Message,
    val messageOwner: MessageOwner,
    val timestamp: Instant
) {
    val isCurrentUser = messageOwner is MessageOwner.Me

    val isUnread = if (!isCurrentUser) {
        (messageOwner as MessageOwner.Other).status != MessageStatus.ReceiverStatus.READ
    } else false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageSheet) return false

        if (messageId != other.messageId) return false

        return true
    }

    override fun hashCode(): Int {
        return messageId.hashCode()
    }
}
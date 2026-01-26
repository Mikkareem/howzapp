package com.techullurgy.howzapp.features.chats.models

sealed interface MessageOwner {
    val owner: ChatParticipant

    data class Me(override val owner: ChatParticipant, val status: MessageStatus.SenderStatus): MessageOwner

    data class Other(override val owner: ChatParticipant, val status: MessageStatus.ReceiverStatus): MessageOwner
}
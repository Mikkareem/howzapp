package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface MessageOwner {
    val owner: ChatParticipant

    data class Me(override val owner: ChatParticipant, val msgStatus: MessageStatus): MessageOwner

    data class Other(override val owner: ChatParticipant, val isReadByMe: Boolean): MessageOwner
}
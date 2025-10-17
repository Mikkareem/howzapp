package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface ChatType {
    data class Direct(
        val me: ChatParticipant,
        val other: ChatParticipant
    ): ChatType

    data class Group(
        val title: String,
        val pictureUrl: String?,
        val originator: ChatParticipant,
        val participants: List<ChatParticipant>
    ): ChatType
}
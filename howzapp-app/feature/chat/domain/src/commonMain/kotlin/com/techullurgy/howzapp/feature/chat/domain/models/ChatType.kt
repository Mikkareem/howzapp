package com.techullurgy.howzapp.feature.chat.domain.models

sealed interface ChatType {
    data class Direct(
        val me: String,
        val other: String
    ): ChatType

    data class Group(
        val title: String,
        val profileUrl: String?
    ): ChatType
}
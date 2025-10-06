package com.techullurgy.howzapp.feature.chat.domain.models

data class ChatInfo(
    val chatId: String,
    val chatType: ChatType,
    val chatTitle: String,
    val chatProfilePicture: String?,
    val originator: ChatParticipant
)
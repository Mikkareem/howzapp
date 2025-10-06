package com.techullurgy.howzapp.feature.chat.domain.models

data class ChatParticipant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String? = null,
    val onlineStatus: OnlineStatus = OnlineStatus.NoOnlineStatus
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChatParticipant) return false

        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}
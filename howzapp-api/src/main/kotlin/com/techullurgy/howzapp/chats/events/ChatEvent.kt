package com.techullurgy.howzapp.chats.events

import com.techullurgy.howzapp.common.types.UserId

sealed interface ChatEvent {
    data class OnNewMessage(val toId: UserId) : ChatEvent
    data class OnDeliveredMessage(val toId: UserId) : ChatEvent
    data class OnReadMessage(val toId: UserId) : ChatEvent
}
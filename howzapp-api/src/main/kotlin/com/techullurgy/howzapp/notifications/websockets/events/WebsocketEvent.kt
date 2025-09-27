package com.techullurgy.howzapp.notifications.websockets.events

import com.techullurgy.howzapp.common.types.UserId

sealed interface WebsocketEvent {
    data class OnUserJoined(val userId: UserId): WebsocketEvent
}
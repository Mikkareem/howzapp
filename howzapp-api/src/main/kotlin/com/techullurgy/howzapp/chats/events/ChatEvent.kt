package com.techullurgy.howzapp.chats.events

import com.techullurgy.howzapp.common.types.ChatId
import com.techullurgy.howzapp.common.types.MessageId
import com.techullurgy.howzapp.common.types.UserId

sealed interface ChatEvent {
    data class OnNewMessage(val messageId: MessageId, val chatId: ChatId, val fromId: UserId): ChatEvent
}
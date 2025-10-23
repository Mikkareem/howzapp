package com.techullurgy.howzapp.chats.infra.mappers

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.GroupChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.models.ChatType
import com.techullurgy.howzapp.chats.models.GroupChat
import com.techullurgy.howzapp.chats.models.DirectChat
import com.techullurgy.howzapp.common.types.UserId
import com.techullurgy.howzapp.users.infra.mappers.toDomain

context(me: UserId)
fun ChatEntity.toDomain(): ChatType {
    return when(this) {
        is OneToOneChatEntity -> DirectChat(
            chatId = id,
            participant1 = participants.filter { it.id == me }.map { it.toDomain() }.first(),
            participant2 = participants.filter { it.id != me }.map { it.toDomain() }.first()
        )
        is GroupChatEntity -> GroupChat(
            chatId = id,
            originator = originator.toDomain(),
            participants = participants.map { it.toDomain() },
            title = title,
            profilePictureUrl = profilePictureUrl
        )
        else -> TODO()
    }
}
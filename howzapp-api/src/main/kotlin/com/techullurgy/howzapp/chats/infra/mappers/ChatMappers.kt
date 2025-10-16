package com.techullurgy.howzapp.chats.infra.mappers

import com.techullurgy.howzapp.chats.infra.database.entities.ChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.GroupChatEntity
import com.techullurgy.howzapp.chats.infra.database.entities.chats.OneToOneChatEntity
import com.techullurgy.howzapp.chats.models.ChatType
import com.techullurgy.howzapp.chats.models.GroupChat
import com.techullurgy.howzapp.chats.models.OneToOneChat
import com.techullurgy.howzapp.users.infra.mappers.toDomain

fun ChatEntity.toDomain(): ChatType {
    return when(this) {
        is OneToOneChatEntity -> OneToOneChat(
            id = id,
            originator = originator.toDomain(),
            participant = participants.map { it.toDomain() }.first { it != originator.toDomain() }
        )
        is GroupChatEntity -> GroupChat(
            id = id,
            originator = originator.toDomain(),
            participants = participants.map { it.toDomain() }.filter { it != originator.toDomain() },
            title = title,
            profilePictureUrl = profilePictureUrl
        )
        else -> TODO()
    }
}
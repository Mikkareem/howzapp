package com.techullurgy.howzapp.feature.chat.data.mappers

import com.techullurgy.howzapp.feature.chat.database.models.SerializableChatType
import com.techullurgy.howzapp.feature.chat.domain.models.ChatType

fun SerializableChatType.toDomain(): ChatType {
    return when(this) {
        is SerializableChatType.Direct -> ChatType.Direct(meId, otherId)
        is SerializableChatType.Group -> ChatType.Group(title, profileUrl)
    }
}

fun ChatType.toSerializable(): SerializableChatType {
    return when(this) {
        is ChatType.Direct -> SerializableChatType.Direct(me, other)
        is ChatType.Group -> SerializableChatType.Group(title, profileUrl)
    }
}
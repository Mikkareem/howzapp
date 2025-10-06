package com.techullurgy.howzapp.feature.chat.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.feature.chat.database.models.SerializableChatType
import kotlinx.serialization.json.Json

class ChatTypeTypeConverter {
    @TypeConverter
    fun fromChatType(chatType: SerializableChatType?): String? {
        return Json.encodeToString(chatType)
    }

    @TypeConverter
    fun toChatType(value: String?): SerializableChatType? {
        return value?.let { Json.decodeFromString(value) }
    }
}
package com.techullurgy.howzapp.features.chats.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.features.chats.database.models.SerializableMessage
import kotlinx.serialization.json.Json

class MessageTypeConverter {
    @TypeConverter
    fun fromMessage(message: SerializableMessage?): String? {
        return Json.encodeToString(message)
    }

    @TypeConverter
    fun toMessage(value: String?): SerializableMessage? {
        return value?.let { Json.decodeFromString(value) }
    }
}
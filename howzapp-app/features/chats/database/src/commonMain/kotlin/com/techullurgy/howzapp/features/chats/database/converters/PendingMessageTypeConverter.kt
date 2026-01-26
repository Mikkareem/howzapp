package com.techullurgy.howzapp.features.chats.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.features.chats.database.models.SerializablePendingMessage
import kotlinx.serialization.json.Json

class PendingMessageTypeConverter {
    @TypeConverter
    fun fromMessage(message: SerializablePendingMessage?): String? {
        return Json.encodeToString(message)
    }

    @TypeConverter
    fun toMessage(value: String?): SerializablePendingMessage? {
        return value?.let { Json.decodeFromString(value) }
    }
}
package com.techullurgy.howzapp.feature.chat.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.feature.chat.database.models.SerializableOnlineStatus
import kotlinx.serialization.json.Json

class OnlineStatusTypeConverter {

    @TypeConverter
    fun fromOnlineStatus(status: SerializableOnlineStatus?): String? {
        return Json.encodeToString(status)
    }

    @TypeConverter
    fun toOnlineStatus(value: String?): SerializableOnlineStatus? {
        return value?.let { Json.decodeFromString(value) }
    }
}
package com.techullurgy.howzapp.features.chats.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.features.chats.database.models.SerializableUploadStatus
import kotlinx.serialization.json.Json

class UploadStatusTypeConverter {

    @TypeConverter
    fun fromUploadStatus(status: SerializableUploadStatus?): String? {
        return Json.encodeToString(status)
    }

    @TypeConverter
    fun toUploadStatus(value: String?): SerializableUploadStatus? {
        return value?.let { Json.decodeFromString(value) }
    }
}
package com.techullurgy.howzapp.feature.chat.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.feature.chat.database.models.SerializableUploadStatus
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
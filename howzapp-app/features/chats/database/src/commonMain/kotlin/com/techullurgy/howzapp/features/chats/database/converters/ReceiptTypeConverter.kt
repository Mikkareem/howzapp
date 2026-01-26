package com.techullurgy.howzapp.features.chats.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.features.chats.database.models.SerializableReceipt
import kotlinx.serialization.json.Json

class ReceiptTypeConverter {
    @TypeConverter
    fun fromReceipt(message: SerializableReceipt?): String? {
        return Json.encodeToString(message)
    }

    @TypeConverter
    fun toReceipt(value: String?): SerializableReceipt? {
        return value?.let { Json.decodeFromString(value) }
    }
}
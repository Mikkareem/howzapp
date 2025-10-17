package com.techullurgy.howzapp.feature.chat.database.converters

import androidx.room.TypeConverter
import com.techullurgy.howzapp.feature.chat.database.models.SerializableReceipt
import kotlinx.serialization.json.Json

class PendingReceiptTypeConverter {
    @TypeConverter
    fun fromReceipt(message: SerializableReceipt?): String? {
        return Json.encodeToString(message)
    }

    @TypeConverter
    fun toReceipt(value: String?): SerializableReceipt? {
        return value?.let { Json.decodeFromString(value) }
    }
}
package com.techullurgy.howzapp.feature.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.techullurgy.howzapp.core.data.util.appDataDirectory
import java.io.File

actual open class DatabaseFactory {
    actual open fun create(): RoomDatabase.Builder<HowzappDatabase> {
        val directory = appDataDirectory

        if(!directory.exists()) {
            directory.mkdirs()
        }

        val dbFile = File(directory, HowzappDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}
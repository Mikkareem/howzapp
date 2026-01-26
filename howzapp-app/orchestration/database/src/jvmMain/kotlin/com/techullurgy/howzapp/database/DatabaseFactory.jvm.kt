package com.techullurgy.howzapp.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.techullurgy.howzapp.core.system.utils.appDataDirectory
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
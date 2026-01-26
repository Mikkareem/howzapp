package com.techullurgy.howzapp.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techullurgy.howzapp.database.DatabaseFactory
import com.techullurgy.howzapp.database.HowzappDatabase

actual open class DatabaseFactory(
    private val context: Context?
) {
    actual open fun create(): RoomDatabase.Builder<HowzappDatabase> {
        val dbFile = context!!.applicationContext.getDatabasePath(HowzappDatabase.DB_NAME)

        return Room.databaseBuilder(
            context.applicationContext,
            dbFile.absolutePath
        )
    }
}

class TestDatabaseFactory(
    private val context: Context
): DatabaseFactory(context) {
    override fun create(): RoomDatabase.Builder<HowzappDatabase> {
        return Room.inMemoryDatabaseBuilder(context, HowzappDatabase::class.java)
    }
}
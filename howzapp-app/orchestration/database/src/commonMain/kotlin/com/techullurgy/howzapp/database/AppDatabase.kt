package com.techullurgy.howzapp.database

import androidx.room.RoomDatabase
import com.techullurgy.howzapp.core.database.AppDatabase

internal class DefaultAppDatabase(
    override val roomDatabase: RoomDatabase
) : AppDatabase()
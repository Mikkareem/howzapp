package com.techullurgy.howzapp.core.database

import androidx.room.RoomDatabase
import com.techullurgy.howzapp.core.database.utils.safeExecute

abstract class AppDatabase protected constructor() {
    protected abstract val roomDatabase: RoomDatabase

    suspend fun safeExecute(
        isReadOnly: Boolean = false,
        block: suspend () -> Unit
    ) {
        roomDatabase.safeExecute(isReadOnly, block)
    }
}
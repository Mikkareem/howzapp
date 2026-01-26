package com.techullurgy.howzapp.database

import androidx.room.RoomDatabase

expect open class DatabaseFactory {
    open fun create(): RoomDatabase.Builder<HowzappDatabase>
}
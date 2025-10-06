package com.techullurgy.howzapp.feature.chat.database

import androidx.room.RoomDatabase

expect open class DatabaseFactory {
    constructor()
    open fun create(): RoomDatabase.Builder<HowzappDatabase>
}
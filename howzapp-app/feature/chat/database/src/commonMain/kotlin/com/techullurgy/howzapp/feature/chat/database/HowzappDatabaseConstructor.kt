package com.techullurgy.howzapp.feature.chat.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object HowzappDatabaseConstructor: RoomDatabaseConstructor<HowzappDatabase> {
    override fun initialize(): HowzappDatabase
}
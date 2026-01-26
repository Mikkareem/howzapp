package com.techullurgy.howzapp.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object HowzappDatabaseConstructor: RoomDatabaseConstructor<HowzappDatabase> {
    override fun initialize(): HowzappDatabase
}
package com.techullurgy.howzapp.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(obj: T)

    @Upsert
    suspend fun upsert(obj: T)

    @Delete
    suspend fun delete(obj: T)
}
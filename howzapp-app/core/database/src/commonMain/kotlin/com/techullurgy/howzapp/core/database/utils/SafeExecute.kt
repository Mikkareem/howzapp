package com.techullurgy.howzapp.core.database.utils

import androidx.room.RoomDatabase
import androidx.room.deferredTransaction
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection

internal suspend fun RoomDatabase.safeExecute(
    isReadOnly: Boolean = false,
    block: suspend () -> Unit
) {
    if(isReadOnly) {
        useReaderConnection {
            if(it.inTransaction()) {
                block()
            } else {
                it.deferredTransaction {
                    block()
                }
            }
        }
    } else {
        useWriterConnection {
            if(it.inTransaction()) {
                block()
            } else {
                it.deferredTransaction {
                    block()
                }
            }
        }
    }
}
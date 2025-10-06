package com.techullurgy.howzapp.feature.chat.database.utils

import androidx.room.RoomDatabase
import androidx.room.deferredTransaction
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection

suspend fun RoomDatabase.safeExecute(
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
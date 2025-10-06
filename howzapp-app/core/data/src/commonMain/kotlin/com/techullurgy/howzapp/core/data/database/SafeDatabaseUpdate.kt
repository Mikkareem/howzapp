package com.techullurgy.howzapp.core.data.database

import androidx.sqlite.SQLiteException
import com.techullurgy.howzapp.core.domain.util.AppResult
import com.techullurgy.howzapp.core.domain.util.DataError

suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): AppResult<T, DataError.Local> {
    return try {
        AppResult.Success(update())
    } catch(_: SQLiteException) {
        AppResult.Failure(DataError.Local.DISK_FULL)
    }
}
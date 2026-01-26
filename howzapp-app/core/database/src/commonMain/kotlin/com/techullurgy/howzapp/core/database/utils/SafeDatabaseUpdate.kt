package com.techullurgy.howzapp.core.database.utils

import androidx.sqlite.SQLiteException
import com.techullurgy.howzapp.common.utils.models.AppResult
import com.techullurgy.howzapp.common.utils.models.DataError

internal suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): AppResult<T, DataError.Local> {
    return try {
        AppResult.Success(update())
    } catch (_: SQLiteException) {
        AppResult.Failure(DataError.Local.DISK_FULL)
    }
}
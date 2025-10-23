package com.techullurgy.howzapp.core.data.logging

import co.touchlab.kermit.Logger
import com.techullurgy.howzapp.core.domain.logging.HowzappLogger
import org.koin.core.annotation.Single

@Single(binds = [HowzappLogger::class])
class DefaultLogger : HowzappLogger {
    override fun debug(message: String) {
        Logger.d { message }
    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(throwable) { message }
    }

    override fun info(message: String) {
        Logger.i { message }
    }

    override fun warn(message: String) {
        Logger.w { message }
    }
}
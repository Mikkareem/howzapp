package com.techullurgy.howzapp.test.utilities

import com.techullurgy.howzapp.core.domain.logging.HowzappLogger

internal class TestLogger : HowzappLogger {
    override fun debug(message: String) {
        println(message)
    }

    override fun info(message: String) {
        println(message)
    }

    override fun warn(message: String) {
        println(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        println(message)
        throwable?.printStackTrace()
    }
}
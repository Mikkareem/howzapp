package com.techullurgy.howzapp.common.utils

interface Logger {
    fun info(message: String)
    fun warn(message: String)
    fun debug(message: String)
    fun error(message: String, e: Throwable? = null)
}
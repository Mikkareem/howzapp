package com.techullurgy.howzapp.core.domain.logging

interface HowzappLogger {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String, throwable: Throwable? = null)
}
package com.techullurgy.howzapp.core.data.util

enum class DesktopOs {
    WINDOWS,
    MAC,
    LINUX
}

internal val currentOs: DesktopOs
    get() {
        val osName = System.getProperty("os.name").lowercase()
        return when {
            osName.contains("win") -> DesktopOs.WINDOWS
            osName.contains("mac") -> DesktopOs.MAC
            else -> DesktopOs.LINUX
        }
    }
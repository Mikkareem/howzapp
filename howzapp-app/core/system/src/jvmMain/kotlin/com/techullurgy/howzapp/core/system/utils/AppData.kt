package com.techullurgy.howzapp.core.system.utils

import java.io.File

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

val appDataDirectory: File
    get() {
        val userHome = System.getProperty("user.home")
        return when (currentOs) {
            DesktopOs.WINDOWS -> File(System.getenv("APPDATA"), "Howzapp")
            DesktopOs.MAC -> File(userHome, "Library/Application Support/Howzapp")
            DesktopOs.LINUX -> File(userHome, ".local/share/Howzapp")
        }
    }
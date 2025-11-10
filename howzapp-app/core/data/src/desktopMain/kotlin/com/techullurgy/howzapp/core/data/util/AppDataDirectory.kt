package com.techullurgy.howzapp.core.data.util

import java.io.File

val appDataDirectory: File
    get() {
        val userHome = System.getProperty("user.home")
        return when(currentOs) {
            DesktopOs.WINDOWS -> File(System.getenv("APPDATA"), "Howzapp")
            DesktopOs.MAC -> File(userHome, "Library/Application Support/Howzapp")
            DesktopOs.LINUX -> File(userHome, ".local/share/Howzapp")
        }
    }
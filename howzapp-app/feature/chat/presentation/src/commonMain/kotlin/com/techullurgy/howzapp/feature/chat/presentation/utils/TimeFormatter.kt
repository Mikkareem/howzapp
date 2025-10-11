package com.techullurgy.howzapp.feature.chat.presentation.utils

import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlin.time.Clock
import kotlin.time.Instant

internal fun Instant.toUIString(): String {
    val elapsedTime = Clock.System.now().toEpochMilliseconds() - toEpochMilliseconds()

    return when {
        elapsedTime < 60000 -> "Just now"
        elapsedTime < 360000 -> "${
            ({
                val minutes = elapsedTime / 60000
                if (minutes == 1L) "1 minute" else "$minutes minutes"
            })()
        } ago"

        else -> format(
            DateTimeComponents.Format {
                amPmHour(); char(':'); minute(); char(' '); amPmMarker("AM", "PM")
            }
        )
    }
}
package com.techullurgy.howzapp.feature.chat.presentation.utils

import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

internal fun Instant.toUIString(): String {
    val elapsedTime = Clock.System.now().toEpochMilliseconds() - toEpochMilliseconds()

    return when {
        elapsedTime < 60000 -> "Just now"
        elapsedTime < 360000 -> {
            val minutes1 = elapsedTime / 60000
            "${
                if (minutes1 == 1L) "1 minute" else "$minutes1 minutes"
            } ago"
        }

        else -> format(
            DateTimeComponents.Format {
                amPmHour(); char(':'); minute(); char(' '); amPmMarker("AM", "PM")
            }
        )
    }
}

internal fun Long.toDurationUiString(): String {
    val minutes = milliseconds.inWholeMinutes.toString().padStart(2, '0')
    val seconds = (milliseconds.inWholeSeconds % 60).toString().padStart(2, '0')

    return "$minutes:$seconds"
}
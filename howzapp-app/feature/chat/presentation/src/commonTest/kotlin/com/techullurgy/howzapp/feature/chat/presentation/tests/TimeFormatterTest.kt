package com.techullurgy.howzapp.feature.chat.presentation.tests

import assertk.assertThat
import assertk.assertions.containsMatch
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import com.techullurgy.howzapp.feature.chat.presentation.utils.toUIString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.atDate
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimeFormatterTest {

    @Test
    fun `test 'Just Now' string is returned, when the current time is less than 1 minute from message time`() {
        assertThat(
            Clock.System.now().toUIString()
        )
            .isEqualTo("Just now")

        assertThat(
            Clock.System.now().minus(61.seconds).toUIString()
        )
            .isNotEqualTo("Just now")
    }

    @Test
    fun `test '1 minute ago' string is returned, when the current time is 1-2 minute from message time`() {
        assertThat(
            Clock.System.now().minus(1.minutes).toUIString()
        )
            .isEqualTo("1 minute ago")

        assertThat(
            Clock.System.now().minus(1.minutes + 30.seconds).toUIString()
        )
            .isEqualTo("1 minute ago")

        assertThat(
            Clock.System.now().minus(1.minutes + 61.seconds).toUIString()
        )
            .isNotEqualTo("1 minute ago")
    }

    @Test
    fun `test '2 minutes ago' string is returned, when the current time is 2-3 minutes from message time`() {
        assertThat(
            Clock.System.now().minus(2.minutes).toUIString()
        )
            .isEqualTo("2 minutes ago")

        assertThat(
            Clock.System.now().minus(2.minutes + 30.seconds).toUIString()
        )
            .isEqualTo("2 minutes ago")

        assertThat(
            Clock.System.now().minus(2.minutes + 61.seconds).toUIString()
        )
            .isNotEqualTo("2 minutes ago")
    }

    @Test
    fun `test '3 minutes ago' string is returned, when the current time is 3-4 minutes from message time`() {
        assertThat(
            Clock.System.now().minus(3.minutes).toUIString()
        )
            .isEqualTo("3 minutes ago")

        assertThat(
            Clock.System.now().minus(3.minutes + 30.seconds).toUIString()
        )
            .isEqualTo("3 minutes ago")

        assertThat(
            Clock.System.now().minus(3.minutes + 61.seconds).toUIString()
        )
            .isNotEqualTo("3 minutes ago")
    }

    @Test
    fun `test '4 minutes ago' string is returned, when the current time is 4-5 minutes from message time`() {
        assertThat(
            Clock.System.now().minus(4.minutes).toUIString()
        )
            .isEqualTo("4 minutes ago")

        assertThat(
            Clock.System.now().minus(4.minutes + 40.seconds).toUIString()
        )
            .isEqualTo("4 minutes ago")

        assertThat(
            Clock.System.now().minus(4.minutes + 61.seconds).toUIString()
        )
            .isNotEqualTo("4 minutes ago")
    }

    @Test
    fun `test '5 minutes ago' string is returned, when the current time is 5-6 minutes from message time`() {
        assertThat(
            Clock.System.now().minus(5.minutes).toUIString()
        )
            .isEqualTo("5 minutes ago")

        assertThat(
            Clock.System.now().minus(5.minutes + 30.seconds).toUIString()
        )
            .isEqualTo("5 minutes ago")

        assertThat(
            Clock.System.now().minus(5.minutes + 61.seconds).toUIString()
        )
            .isNotEqualTo("5 minutes ago")
    }

    @Test
    fun `test 'AM-PM' string is returned, when the current time is more than 6 minutes from message time`() {
        assertThat(
            Clock.System.now().minus(5.minutes + 61.seconds).toUIString()
        ).doesNotContain("minutes ago")

        assertThat(
            Clock.System.now().minus(5.minutes + 61.seconds).toUIString()
        ).containsMatch(Regex("""\d{2}:\d{2} [A|P]M"""))
    }

    @Test
    fun `test Exact Time string is returned with AM-PM, when the current time is more than 5 minutes from message time`() {
        val commonDate = LocalDate(1997, Month.OCTOBER, 31)

        assertThat(
            LocalTime(23, 59).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("11:59 PM")

        assertThat(
            LocalTime(0, 0).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:00 AM")

        assertThat(
            LocalTime(0, 1).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:01 AM")

        assertThat(
            LocalTime(11, 59).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("11:59 AM")

        assertThat(
            LocalTime(12, 0).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:00 PM")

        assertThat(
            LocalTime(12, 1).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:01 PM")

        assertThat(
            LocalTime(14, 24).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("02:24 PM")

        assertThat(
            LocalTime(12, 24).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:24 PM")

        assertThat(
            LocalTime(0, 24).atDate(commonDate).toInstant(UtcOffset.ZERO).toUIString()
        )
            .isEqualTo("12:24 AM")
    }
}
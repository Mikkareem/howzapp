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
    fun test_JustNow_string_is_returned_when_the_current_time_is_less_than_1_minute_from_message_time() {
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
    fun test_1_minute_ago_string_is_returned_when_the_current_time_is_1_to_2_minutes_from_message_time() {
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
    fun test_2_minutes_ago_string_is_returned_when_the_current_time_is_2_to_3_minutes_from_message_time() {
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
    fun test_3_minutes_ago_string_is_returned_when_the_current_time_is_3_to_4_minutes_from_message_time() {
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
    fun test_4_minutes_ago_string_is_returned_when_the_current_time_is_4_to_5_minutes_from_message_time() {
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
    fun test_5_minutes_ago_string_is_returned_when_the_current_time_is_5_to_6_minutes_from_message_time() {
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
    fun test_AM_PM_string_is_returned_when_the_current_time_is_more_than_6_minutes_from_message_time() {
        assertThat(
            Clock.System.now().minus(5.minutes + 61.seconds).toUIString()
        ).doesNotContain("minutes ago")

        assertThat(
            Clock.System.now().minus(5.minutes + 61.seconds).toUIString()
        ).containsMatch(Regex("""\d{2}:\d{2} [A|P]M"""))
    }

    @Test
    fun test_Exact_Time_string_is_returned_with_AM_PM_when_the_current_time_is_more_than_5_minutes_from_message_time() {
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
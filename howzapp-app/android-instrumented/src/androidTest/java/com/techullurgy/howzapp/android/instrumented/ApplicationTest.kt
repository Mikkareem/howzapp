package com.techullurgy.howzapp.android.instrumented

import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import kotlin.test.BeforeTest
import kotlin.test.Test

class ApplicationTest {

    @BeforeTest
    fun setup() {
        uiAutomator { pressHome() }
    }

    @Test
    fun test() {
        uiAutomator {
            startApp("com.techullurgy.howzapp.prod")

            onElement { textAsString()?.contains("Irsath") == true }.click()

            device.waitForIdle()
        }
    }

    @Test
    fun test2() {
        uiAutomator {
            startApp("com.techullurgy.howzapp.prod")

            onElement { textAsString()?.contains("Irsath") == true }.click()

            device.waitForIdle()
        }
    }
}
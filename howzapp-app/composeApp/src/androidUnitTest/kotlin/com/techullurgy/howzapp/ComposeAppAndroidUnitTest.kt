package com.techullurgy.howzapp

import com.techullurgy.howzapp.feature.chat.test.di.chatTestDomainModule
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.configurationModules
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class ComposeAppAndroidUnitTest: KoinTest {

    @get:Rule
    val koin = KoinTestRule.create {
        androidContext(RuntimeEnvironment.getApplication())
        modules(DevelopmentApp.configurationModules + chatTestDomainModule)
    }

    @Test
    fun example() {

    }
}
package com.techullurgy.howzapp

import com.techullurgy.howzapp.test.utilities.testModule
import org.koin.ksp.generated.startKoin
import kotlin.test.Test

class ComposeAppAndroidUnitTest {

    @Test
    fun example() {
        DevelopmentApp.startKoin {
            modules(testModule)
        }
    }
}
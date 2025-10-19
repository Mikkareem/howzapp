package com.techullurgy.howzapp

import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import kotlin.test.Test

class ComposeAppAndroidUnitTest {

    @Test
    fun example() {
        val scope = startKoin {
            AppModule().module
        }.koin

    }
}
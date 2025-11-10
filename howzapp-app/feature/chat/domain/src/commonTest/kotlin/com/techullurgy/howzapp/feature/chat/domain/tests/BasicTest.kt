package com.techullurgy.howzapp.feature.chat.domain.tests

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class BasicTest {

    @Test
    fun basicTest(): Unit = runBlocking {
        val comp = UnderTest<Int>(this)

        launch {
            comp.state.collect {
                println(it)
            }
        }

        repeat(10) {
            comp.send(it+1)
            delay(1000)
        }
    }
}

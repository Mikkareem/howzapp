package com.techullurgy.howzapp.feature.chat.data

import com.techullurgy.howzapp.feature.chat.data.di.chatDataModule
import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatNetworkRepository
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test

class BasicKoinTest: KoinTest {

    @Test
    fun basicKoinTest() {
        startKoin {
            modules(
                chatDataModule,
            )
        }

        get<ChatNetworkRepository>()
    }
}
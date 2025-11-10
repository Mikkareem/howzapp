package com.techullurgy.howzapp.feature.chat.data

import com.techullurgy.howzapp.core.domain.networking.NetworkUploadStatus
import com.techullurgy.howzapp.core.domain.networking.UploadClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

class BasicKoinTest: KoinTest {
    private val module = module {
        single<UploadClient<Int>> {
            object : UploadClient<Int>() {
                override fun uploadMedia(
                    source: Int,
                    sourceLength: Long,
                    targetUrl: String,
                    requestHeaders: Map<String, String>
                ): Flow<NetworkUploadStatus> {
                    println(1)
                    return flow {  }
                }
            }
        }

        single<UploadClient<String>> {
            object : UploadClient<String>() {
                override fun uploadMedia(
                    source: String,
                    sourceLength: Long,
                    targetUrl: String,
                    requestHeaders: Map<String, String>
                ): Flow<NetworkUploadStatus> {
                    println(2)
                    return flow {  }
                }
            }
        }

        single<List<Int>> { listOf(1,2,3) }
        single<List<String>> { listOf("11","12","13") }
    }

    @BeforeTest
    fun setup() {
        startKoin {
            modules(module)
        }
    }

    @Test
    fun basicKoinTest() = runTest {
        module.verify()
    }
}
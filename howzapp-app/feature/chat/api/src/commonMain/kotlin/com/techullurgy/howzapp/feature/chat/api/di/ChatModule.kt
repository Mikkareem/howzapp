package com.techullurgy.howzapp.feature.chat.api.di

import com.techullurgy.howzapp.feature.chat.database.di.ChatDatabaseModule
import com.techullurgy.howzapp.feature.chat.domain.di.ChatTestModule
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [ChatDatabaseModule::class])
@Configuration
@ComponentScan("com.techullurgy.howzapp.feature.chat")
class ChatModule {
    @Single
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }
}

@Module(includes = [ChatTestModule::class])
@Configuration("test")
class ChatModuleForTest
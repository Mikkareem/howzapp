package com.techullurgy.howzapp.feature.chat.di

import com.techullurgy.howzapp.feature.chat.data.di.ChatDataModule
import com.techullurgy.howzapp.feature.chat.database.di.ChatDatabaseModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        ChatDatabaseModule::class,
        ChatDataModule::class
    ]
)
class ChatModule
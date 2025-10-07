package com.techullurgy.howzapp.feature.chat.api.di

import com.techullurgy.howzapp.feature.chat.data.di.chatDataModule
import com.techullurgy.howzapp.feature.chat.database.di.chatDatabaseModule
import com.techullurgy.howzapp.feature.chat.presentation.di.chatPresentationModule
import org.koin.dsl.module

val chatModule = module {
    includes(chatDataModule, chatDatabaseModule, chatPresentationModule)
}
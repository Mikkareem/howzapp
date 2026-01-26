package com.techullurgy.howzapp.features.chats.di

import com.techullurgy.howzapp.features.chats.data.di.ChatDataModule
import com.techullurgy.howzapp.features.chats.domain.di.ChatDomainModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        ChatDataModule::class,
        ChatDomainModule::class
    ]
)
class FeatureChatsModule
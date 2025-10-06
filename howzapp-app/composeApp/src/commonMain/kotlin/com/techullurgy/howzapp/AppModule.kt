package com.techullurgy.howzapp

import com.techullurgy.howzapp.core.di.CoreModule
import com.techullurgy.howzapp.feature.chat.di.ChatModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreModule::class,
        ChatModule::class
    ],
)
@ComponentScan("com.techullurgy.howzapp**")
class AppModule
package com.techullurgy.howzapp

import com.techullurgy.howzapp.core.di.coreModule
import com.techullurgy.howzapp.feature.chat.api.di.chatModule
import org.koin.dsl.module

val appModule = module {
    includes(
        coreModule,
        chatModule
    )
}
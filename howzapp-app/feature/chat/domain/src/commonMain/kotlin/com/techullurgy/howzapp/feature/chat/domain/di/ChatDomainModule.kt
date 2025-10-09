package com.techullurgy.howzapp.feature.chat.domain.di

import com.techullurgy.howzapp.feature.chat.domain.repositories.ChatRepository
import com.techullurgy.howzapp.feature.chat.domain.repositories.TestRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val testModule = module(createdAtStart = true) {
    // TODO: For testing only
    singleOf(::TestRepository)
}

val chatDomainModule = module {
    singleOf(::ChatRepository)

    // TODO: For testing only
    includes(testModule)
}
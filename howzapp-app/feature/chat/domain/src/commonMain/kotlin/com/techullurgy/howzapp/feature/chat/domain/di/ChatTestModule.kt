package com.techullurgy.howzapp.feature.chat.domain.di

import com.techullurgy.howzapp.feature.chat.domain.repositories.TestRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module(createdAtStart = true)
class ChatTestModule {
    @Single
    internal fun provideTestRepository(scope: Scope): TestRepository {
        return TestRepository(
            scope.get(),
            scope.get(),
            scope.get()
        )
    }
}
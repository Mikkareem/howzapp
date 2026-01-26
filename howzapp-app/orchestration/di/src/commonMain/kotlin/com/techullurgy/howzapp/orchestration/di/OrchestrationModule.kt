package com.techullurgy.howzapp.orchestration.di

import com.techullurgy.howzapp.common.utils.di.CommonUtilsModule
import com.techullurgy.howzapp.core.di.CoreModule
import com.techullurgy.howzapp.database.di.DatabaseModule
import com.techullurgy.howzapp.features.auth.di.FeatureAuthModule
import com.techullurgy.howzapp.features.chats.di.FeatureChatsModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        CommonUtilsModule::class,
        CoreModule::class,
        DatabaseModule::class,

        FeatureAuthModule::class,
        FeatureChatsModule::class
    ]
)
class OrchestrationModule
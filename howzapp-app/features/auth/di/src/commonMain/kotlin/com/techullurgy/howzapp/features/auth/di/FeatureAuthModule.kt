package com.techullurgy.howzapp.features.auth.di

import org.koin.core.annotation.Module
import com.techullurgy.howzapp.features.auth.data.di.AuthDataModule

@Module(
    includes = [
        AuthDataModule::class
    ]
)
class FeatureAuthModule
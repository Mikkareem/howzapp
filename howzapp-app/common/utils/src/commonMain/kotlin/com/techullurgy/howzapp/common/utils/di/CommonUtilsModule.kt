package com.techullurgy.howzapp.common.utils.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoroutinesModule::class
    ]
)
@ComponentScan("com.techullurgy.howzapp.common.utils")
class CommonUtilsModule
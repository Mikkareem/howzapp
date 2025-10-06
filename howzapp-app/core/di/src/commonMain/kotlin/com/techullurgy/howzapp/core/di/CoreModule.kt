package com.techullurgy.howzapp.core.di

import com.techullurgy.howzapp.core.data.di.coreDataModule
import org.koin.dsl.module

val coreModule = module {
    includes(coreDataModule)
}
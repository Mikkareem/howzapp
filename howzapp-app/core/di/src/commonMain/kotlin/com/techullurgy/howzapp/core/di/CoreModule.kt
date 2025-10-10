package com.techullurgy.howzapp.core.di

import com.techullurgy.howzapp.core.data.di.CoreDataModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [CoreDataModule::class])
@Configuration
@ComponentScan("com.techullurgy.howzapp.core")
class CoreModule
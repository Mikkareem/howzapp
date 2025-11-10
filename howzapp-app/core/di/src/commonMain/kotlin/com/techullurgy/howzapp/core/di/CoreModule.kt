package com.techullurgy.howzapp.core.di

import com.techullurgy.howzapp.core.data.di.CoreDataModule
import com.techullurgy.howzapp.core.system.di.CoreSystemModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [CoreDataModule::class, CoreSystemModule::class])
@Configuration
@ComponentScan("com.techullurgy.howzapp.core")
class CoreModule
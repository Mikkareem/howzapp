package com.techullurgy.howzapp.core.di

import com.techullurgy.howzapp.core.data.impl.di.CoreDataModule
import com.techullurgy.howzapp.core.datastore.di.CoreDataStoreModule
import com.techullurgy.howzapp.core.network.di.CoreNetworkModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreDataModule::class,
        CoreDataStoreModule::class,
        CoreNetworkModule::class
    ]
)
class CoreModule
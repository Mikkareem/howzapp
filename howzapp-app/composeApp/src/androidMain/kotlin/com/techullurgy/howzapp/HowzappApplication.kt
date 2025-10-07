package com.techullurgy.howzapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HowzappApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HowzappApplication)
            modules(appModule)
        }
    }
}
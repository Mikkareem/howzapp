package com.techullurgy.howzapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.startKoin

class HowzappApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        DebugApp.startKoin {
            androidContext(this@HowzappApplication)
        }
    }
}
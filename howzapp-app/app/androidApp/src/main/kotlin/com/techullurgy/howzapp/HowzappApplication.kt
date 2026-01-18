package com.techullurgy.howzapp

import android.app.Application
import org.koin.ksp.generated.startKoin

class HowzappApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        DebugApp.startKoin {
            initApp(this@HowzappApplication)
        }
    }
}
package com.techullurgy.howzapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinApplication
import org.koin.ksp.generated.startKoin

@KoinApplication(
    configurations = ["default", "test"]
)
object ProductionApp

@KoinApplication(
    configurations = ["default"]
)
object DevelopmentApp

class HowzappApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        DevelopmentApp.startKoin {
            androidContext(this@HowzappApplication)
        }
    }
}
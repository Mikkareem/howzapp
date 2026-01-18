package com.techullurgy.howzapp

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinApplication

@KoinApplication(
    configurations = ["default"]
)
object ProductionApp

@KoinApplication(
    configurations = ["default", "debug"]
)
object DebugApp

@KoinApplication(
    configurations = ["default", "test"]
)
object DevelopmentApp

fun org.koin.core.KoinApplication.initApp(
    applicationContext: Context
) {
    androidContext(applicationContext)
}
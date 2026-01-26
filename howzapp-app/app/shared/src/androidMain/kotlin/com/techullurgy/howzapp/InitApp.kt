package com.techullurgy.howzapp

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
package com.techullurgy.howzapp.test.utilities

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestApplicationRunner: AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {
        return super.newApplication(classLoader, TestApplication::class.java.name, context)
    }
}

internal class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)

            modules(testModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}
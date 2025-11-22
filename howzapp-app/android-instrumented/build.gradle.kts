plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.techullurgy.android.instrumented"
    compileSdk {
        version = release(applicationLibs.versions.android.compileSdk.get().toInt())
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
        targetCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
    }

    kotlin {
        jvmToolchain(applicationLibs.versions.javaVersion.get().toInt())
    }

    defaultConfig {
        minSdk = applicationLibs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    androidTestImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.kotlin.test)
}
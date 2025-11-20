plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.techullurgy.howzapp.android.benchmark"
    compileSdk {
        version = release(applicationLibs.versions.android.compileSdk.get().toInt())
    }

    defaultConfig {
        minSdk = applicationLibs.versions.android.minSdk.get().toInt()
        targetSdk = applicationLibs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":composeApp"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
        targetCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
    }
}

kotlin {
    jvmToolchain(applicationLibs.versions.javaVersion.get().toInt())
}

dependencies {
    implementation(libs.androidx.testExt.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
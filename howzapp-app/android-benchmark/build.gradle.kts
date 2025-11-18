import com.android.build.api.dsl.ManagedVirtualDevice

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

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
            proguardFile("benchmark-rules.pro")
        }
    }

    targetProjectPath = ":composeApp"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
        targetCompatibility =
            JavaVersion.toVersion(applicationLibs.versions.javaVersion.get().toInt())
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel6") {
                    apiLevel = 35
                    device = "Pixel 6"
                    systemImageSource = "aosp"
                }
            }
        }
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

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.techullurgy.android.baselineprofile"
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
        targetSdk = applicationLibs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":composeApp"

}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.testExt.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}
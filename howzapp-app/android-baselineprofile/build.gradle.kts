import java.util.Properties

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.baselineprofile)
}

private val configProperties = Properties().apply {
    val configPropertiesFile = rootProject.file("config.properties")
    if (configPropertiesFile.exists()) {
        configPropertiesFile.reader().use { load(it) }
    }
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

    buildTypes {
        create("release") {}
    }

    defaultConfig {
        minSdk = applicationLibs.versions.android.minSdk.get().toInt()
        targetSdk = applicationLibs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunnerArguments["androidx.benchmark.enabledRules"] =
            "MacroBenchmark" // ""BaselineProfile" // MacroBenchmark

        // While this allows you to run benchmarks on an Android emulator for convenience,
        // please be aware that for accurate performance measurements, you should always use
        // a physical device. An emulator's simulated environment can significantly differ from
        // real-world hardware, potentially skewing your results.
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"

    }

    targetProjectPath = ":composeApp"
    // Enable the benchmark to run separately from the app process
    experimentalProperties["android.experimental.self-instrumenting"] = true

}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    val canUseConnectedDevice =
        configProperties["BASELINE_PROFILE_USE_CONNECTED_DEVICES"]?.toString()
            ?.toBooleanStrictOrNull() ?: false
    if (!canUseConnectedDevice) {
        managedDevices += "pixel6"
    }
    useConnectedDevices = canUseConnectedDevice
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
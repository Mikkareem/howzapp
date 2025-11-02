package com.techullurgy.howzapp.conventions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        compileSdk = applicationLibs.findVersion("android-compileSdk").get().toString().toInt()

        defaultConfig.minSdk = applicationLibs.findVersion("android-minSdk").get().toString().toInt()
//        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        defaultConfig.testInstrumentationRunner = "com.techullurgy.howzapp.test.utilities.TestApplicationRunner"

        compileOptions {
            sourceCompatibility = this@configureKotlinAndroid.javaVersion
            targetCompatibility = this@configureKotlinAndroid.javaVersion
            isCoreLibraryDesugaringEnabled = true
        }

        configureKotlin()

        dependencies {
            "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
        }
    }
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(this@configureKotlin.jvmTarget)

            optIn.addAll(
                "kotlinx.coroutines.ExperimentalCoroutinesApi",
                "kotlinx.coroutines.FlowPreview"
            )
        }
    }
}
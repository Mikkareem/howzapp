package com.techullurgy.howzapp.conventions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension
) {
    with(commonExtension) {
        compileSdk {
            version = release(applicationLibs.findVersion("android-compileSdk").get().toString().toInt())
        }

        defaultConfig.minSdk {
            version = release(applicationLibs.findVersion("android-minSdk").get().toString().toInt())
        }

        compileOptions.sourceCompatibility = this@configureKotlinAndroid.javaVersion
        compileOptions.targetCompatibility = this@configureKotlinAndroid.javaVersion
        compileOptions.isCoreLibraryDesugaringEnabled = true

        packaging.resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }

        testOptions.unitTests {
            isIncludeAndroidResources = true
        }

        configureKotlin()

        configureAndroidProductFlavors()

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
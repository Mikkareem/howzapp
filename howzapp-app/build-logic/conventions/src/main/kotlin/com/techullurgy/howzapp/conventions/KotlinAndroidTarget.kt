package com.techullurgy.howzapp.conventions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(extension: KotlinMultiplatformExtension)
internal fun Project.configureAndroidTarget() {
    extension.extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
        namespace = pathToPackageName()

        compileSdk {
            version = release(applicationLibs.findVersion("android-compileSdk").get().toString().toInt())
        }
        minSdk {
            version = release(applicationLibs.findVersion("android-minSdk").get().toString().toInt())
        }
        androidResources.enable = true

        withHostTest {
            isIncludeAndroidResources = true
        }

        withDeviceTest {

        }
    }
}
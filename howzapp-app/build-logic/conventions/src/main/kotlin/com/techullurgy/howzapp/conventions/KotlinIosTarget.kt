package com.techullurgy.howzapp.conventions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

context(extension: KotlinMultiplatformExtension)
internal fun Project.configureIosTargets(isLibrary: Boolean = false) {
    with(extension) {
        listOf(
//            iosX64(),
            iosArm64(),
//            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = if(!isLibrary) "ComposeApp" else this@configureIosTargets.pathToFrameworkName()
                if(!isLibrary) {
                    isStatic = true
                }
            }
        }
    }
}
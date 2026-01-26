package com.techullurgy.howzapp.conventions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

context(extension: KotlinMultiplatformExtension)
internal fun Project.configureKotlinMultiplatform() {
    with(extension) {
        if(isHierarchyEnabled) {
            applyDefaultHierarchyTemplate()
        }

        if (isAndroidEnabled) {
            configureAndroidTarget()
        }
        if (isIosEnabled) {
            configureIosTargets(true)
        }
        if (isJvmEnabled) {
            configureDesktopTarget()
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}
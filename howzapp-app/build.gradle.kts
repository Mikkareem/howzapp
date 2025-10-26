plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.roborazzi) apply false

    alias(applicationLibs.plugins.conventions.android.application) apply false
    alias(applicationLibs.plugins.conventions.android.application.compose) apply false
    alias(applicationLibs.plugins.conventions.cmp.application) apply false
    alias(applicationLibs.plugins.conventions.kmp.library) apply false
    alias(applicationLibs.plugins.conventions.cmp.library) apply false
    alias(applicationLibs.plugins.conventions.cmp.feature) apply false
    alias(applicationLibs.plugins.conventions.room) apply false
    alias(applicationLibs.plugins.conventions.koin.compiler) apply false
}
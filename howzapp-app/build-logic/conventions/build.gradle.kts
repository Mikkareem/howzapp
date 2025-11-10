import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

val groupNamePrefix = applicationLibs.versions.projectApplicationId.get().toString()

group = "$groupNamePrefix.buildlogic.convention"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.androidx.room.gradle.plugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
}

java {
    val javaVersion = applicationLibs.versions.javaVersion.get().toString().toInt()
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
    compilerOptions {
        val javaTarget = applicationLibs.versions.javaVersion.get().toString()
        jvmTarget = JvmTarget.fromTarget(javaTarget)
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "$groupNamePrefix.conventions.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidComposeApplication") {
            id = "$groupNamePrefix.conventions.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("cmpApplication") {
            id = "$groupNamePrefix.conventions.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }
        register("kmpLibrary") {
            id = "$groupNamePrefix.conventions.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("cmpLibrary") {
            id = "$groupNamePrefix.conventions.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }
        register("cmpFeature") {
            id = "$groupNamePrefix.conventions.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }
        register("buildKonfig") {
            id = "$groupNamePrefix.conventions.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
        register("room") {
            id = "$groupNamePrefix.conventions.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("koinCompiler") {
            id = "$groupNamePrefix.conventions.koin.compiler"
            implementationClass = "KoinAnnotationsConventionPlugin"
        }
    }
}
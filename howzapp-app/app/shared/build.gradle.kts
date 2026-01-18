plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.roborazzi)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.runtime)

            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)

            implementation(projects.core.di)
            implementation(projects.core.designsystem)
            implementation(projects.feature.splash.api)
            implementation(projects.feature.auth.api)
            implementation(projects.feature.chat.api)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidDeviceTest.dependencies {
            implementation(libs.mockk.android)
        }

        androidHostTest.dependencies {
            implementation(libs.mockk.android)
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(libs.roborazzi.compose)
            implementation(libs.roborazzi.rule)

            implementation(libs.mockwebserver)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.websockets)
            implementation(libs.kotlinx.serialization.json)

            implementation(projects.testUtilities)
            implementation(projects.core.presentation)
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.dto)
            implementation(projects.feature.chat.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
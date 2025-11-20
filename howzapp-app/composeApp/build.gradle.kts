plugins {
    alias(applicationLibs.plugins.conventions.cmp.application)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.baselineprofile)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

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

        androidInstrumentedTest.dependencies {
            implementation(libs.mockk.android)
        }

        androidUnitTest.dependencies {
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

android {
    packaging {
        resources {
            excludes += arrayOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    baselineProfile(projects.androidBaselineprofile)
    debugImplementation(compose.uiTooling)
    androidTestImplementation(libs.androidx.compose.uitest.junit4.android)
    testImplementation(libs.androidx.compose.uitest.junit4.android)
    debugImplementation(libs.androidx.compose.uitest.manifest)
}

baselineProfile {
    val isDuringBuild = false

    automaticGenerationDuringBuild = isDuringBuild
    saveInSrc = true
}

tasks.configureEach {
    val variant =
        Regex("ksp(\\w+)KotlinAndroid").matchEntire(name)?.groupValues[1] ?: return@configureEach
    dependsOn(
        "generateResourceAccessorsForAndroid$variant",
        "generateResourceAccessorsForAndroidMain",
        "generateActualResourceCollectorsForAndroidMain",
        "generateComposeResClass",
        "generateResourceAccessorsForCommonMain",
        "generateExpectResourceCollectorsForCommonMain"
    )
}
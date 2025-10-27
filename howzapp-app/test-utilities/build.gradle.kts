plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.testExt.junit)
            api(libs.androidx.testRunner)
            api(libs.robolectric)

            api(libs.koin.android)
        }

        commonMain.dependencies {
            api(libs.kotlinx.coroutines.test)
            api(libs.kotlin.test)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.test.junit4)

            api(libs.assertk)
            api(libs.mockk)

            implementation(projects.core.data)
            implementation(projects.core.dto)
            api(libs.mockwebserver)
        }
    }
}
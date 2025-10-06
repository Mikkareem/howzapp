plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.testExt.junit)
            api(libs.androidx.testRunner)

            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            api(libs.kotlinx.coroutines.test)
            api(libs.kotlin.test)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.test.junit4)

            api(libs.assertk)

            implementation(projects.core.data)
        }
    }
}
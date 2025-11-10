plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.room)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.room.testing)

            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.testRunner)
        }

        desktopMain.dependencies {
            implementation(projects.core.data)
        }
    }
}
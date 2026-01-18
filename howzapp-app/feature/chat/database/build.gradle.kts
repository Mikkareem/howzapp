plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.room)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidDeviceTest.dependencies {
            implementation(libs.androidx.room.testing)

            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.testRunner)
        }

        desktopMain.dependencies {
            implementation(projects.core.data)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.room)
}

kotlin {
    sourceSets {

        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }

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
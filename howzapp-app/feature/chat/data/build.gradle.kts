
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.mockk.android)
        }

        commonMain.dependencies {
            implementation(libs.ktor.client.websockets)

            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.dto)
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.database)
        }

        commonTest.dependencies {
            implementation(projects.testUtilities)
            implementation(projects.core.di)
        }
    }
}
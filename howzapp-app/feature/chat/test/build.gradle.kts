plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.testUtilities)
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            api(projects.feature.chat.domain)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.database)
        }
    }
}
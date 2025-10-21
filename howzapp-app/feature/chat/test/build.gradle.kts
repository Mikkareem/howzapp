plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mockk)
            implementation(libs.koin.core)
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.data)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)

            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.dto)

            implementation(projects.feature.auth.domain)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.auth.presentation)
            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.data)
        }
    }
}
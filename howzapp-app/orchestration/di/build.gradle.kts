plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.chats.di)
            implementation(projects.features.auth.di)

            implementation(projects.common.utils)
            implementation(projects.core.di)
            implementation(projects.orchestration.database)
        }
    }
}
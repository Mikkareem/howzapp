plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.chats.domain)
            implementation(projects.features.chats.database)

            implementation(projects.core.database)
            implementation(projects.common.dto)
            implementation(projects.common.utils)
            implementation(projects.core.data.api)

            implementation(libs.kotlinx.serialization.json)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(applicationLibs.plugins.conventions.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.system)
            implementation(projects.core.database)
            implementation(projects.common.utils)

            implementation(projects.features.chats.database)
        }
    }
}
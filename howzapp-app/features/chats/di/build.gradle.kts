plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.chats.domain)
            implementation(projects.features.chats.data)
            implementation(projects.features.chats.presentation)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.chats.api.ui)
            implementation(projects.features.chats.domain)
        }
    }
}
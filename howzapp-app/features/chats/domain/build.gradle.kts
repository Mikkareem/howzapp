plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.chats.models)

            implementation(projects.core.data.api)
            implementation(projects.core.session)

            implementation(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutines.core)
        }
    }
}
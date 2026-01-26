plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.auth.models)
            api(projects.common.utils)

            implementation(libs.kotlinx.serialization.json)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.database)

            implementation(libs.androidx.room.runtime)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
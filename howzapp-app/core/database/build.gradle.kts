plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.utils)

            implementation(libs.androidx.room.runtime)
        }
    }
}
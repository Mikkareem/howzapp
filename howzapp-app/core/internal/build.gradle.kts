plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.annotations)
        }
    }
}
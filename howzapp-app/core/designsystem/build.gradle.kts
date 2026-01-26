plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
        }
    }
}
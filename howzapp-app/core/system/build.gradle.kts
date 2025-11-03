plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.media3.exoplayer)
        }

        commonMain.dependencies {
            implementation(projects.core.internal)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.navigation3.runtime)

            implementation(projects.feature.splash.domain)
            implementation(projects.core.utils)
        }
    }
}
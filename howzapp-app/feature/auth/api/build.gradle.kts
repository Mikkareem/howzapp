plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.navigation3.runtime)

            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.data)

            implementation(projects.core.utils)
        }
    }
}
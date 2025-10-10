plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.navigation3.runtime)

            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.database)
            implementation(projects.feature.chat.presentation)
        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.navigation3.runtime)

            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.database)
            implementation(projects.feature.chat.presentation)
        }
    }
}
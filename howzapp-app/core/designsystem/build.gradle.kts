plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.media3.ui)
            implementation(libs.media3.ui.compose)
        }

        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)

            implementation(projects.core.presentation)
            implementation(projects.core.system)
        }
    }
}
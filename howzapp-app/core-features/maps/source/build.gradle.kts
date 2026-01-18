plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.maps.compose)
        }

        commonMain.dependencies {
            implementation(libs.ktor.http)

            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)

            implementation(projects.coreFeatures.maps.api)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.components.resources)
            implementation(libs.material3.adaptive)

            implementation(projects.core.domain)
        }
    }
}

compose.resources {
    publicResClass = true
}
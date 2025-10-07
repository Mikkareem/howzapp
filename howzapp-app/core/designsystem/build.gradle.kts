plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}
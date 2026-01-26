plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.network)
            api(projects.common.models)
        }
    }
}
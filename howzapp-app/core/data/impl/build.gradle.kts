plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.process)
        }

        commonMain.dependencies {
            api(projects.core.data.api)
            implementation(projects.core.session)
        }
    }
}
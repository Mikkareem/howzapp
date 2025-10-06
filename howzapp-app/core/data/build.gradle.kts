plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)

            implementation(libs.bundles.ktor.common)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(projects.core.domain)
        }
    }
}
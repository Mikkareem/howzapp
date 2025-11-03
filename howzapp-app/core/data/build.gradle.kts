plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)

            implementation(libs.bundles.ktor.common)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kermit)

            implementation(projects.core.domain)
            implementation(projects.core.internal)
        }
    }
}

plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
}

kotlin {
    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.process)
        }

        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.ktor.client.websockets)

            implementation(projects.core.domain)
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.database)
        }

        commonTest.dependencies {
            implementation(projects.testUtilities)
            implementation(projects.core.di)
        }
    }
}
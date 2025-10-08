plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose.viewmodel)
            implementation(compose.components.resources)
            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)

            implementation(projects.core.designsystem)
            implementation(projects.feature.chat.domain)

        }
    }
}
plugins {
    alias(applicationLibs.plugins.conventions.android.application.compose)
    alias(libs.plugins.secrets)
//    alias(libs.plugins.baselineprofile)
}

dependencies {
    implementation(projects.app.shared)

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.tooling.preview)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

//    implementation(libs.androidx.profileinstaller)
//    baselineProfile(projects.androidBaselineprofile)
    androidTestImplementation(libs.androidx.compose.uitest.junit4.android)
    testImplementation(libs.androidx.compose.uitest.junit4.android)
    debugImplementation(libs.androidx.compose.uitest.manifest)
}

//baselineProfile {
//    automaticGenerationDuringBuild = false
//
//    warnings {
//        maxAgpVersion = false
//    }
//}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
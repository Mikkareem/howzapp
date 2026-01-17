plugins {
    alias(applicationLibs.plugins.conventions.android.application.compose)
    alias(libs.plugins.baselineprofile)
}

baselineProfile {
    warnings {
        maxAgpVersion = false
    }
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    baselineProfile(projects.androidBaselineprofile)
    androidTestImplementation(libs.androidx.compose.uitest.junit4.android)
    testImplementation(libs.androidx.compose.uitest.junit4.android)
    debugImplementation(libs.androidx.compose.uitest.manifest)
}

baselineProfile {
    automaticGenerationDuringBuild = false
}
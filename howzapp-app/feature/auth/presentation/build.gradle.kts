plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.koin.compose.viewmodel)
            implementation(compose.components.resources)

            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            implementation(projects.feature.auth.domain)
        }
    }
}

afterEvaluate {
    tasks.named("kspDebugKotlinAndroid") {
        dependsOn(
            "generateResourceAccessorsForAndroidDebug",
            "generateResourceAccessorsForAndroidMain",
            "generateActualResourceCollectorsForAndroidMain",
            "generateComposeResClass",
            "generateResourceAccessorsForCommonMain",
            "generateExpectResourceCollectorsForCommonMain"
        )
    }

    tasks.named("kspReleaseKotlinAndroid") {
        dependsOn(
            "generateResourceAccessorsForAndroidRelease",
            "generateResourceAccessorsForAndroidMain",
            "generateActualResourceCollectorsForAndroidMain",
            "generateComposeResClass",
            "generateResourceAccessorsForCommonMain",
            "generateExpectResourceCollectorsForCommonMain"
        )
    }
}
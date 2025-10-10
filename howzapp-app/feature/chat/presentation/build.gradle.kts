plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.koin.compose.viewmodel)
            implementation(compose.components.resources)
            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)

            implementation(projects.core.designsystem)
            implementation(projects.core.domain)
            implementation(projects.feature.chat.domain)

        }
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
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
}
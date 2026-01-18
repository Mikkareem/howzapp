plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.components.resources)
            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)
            implementation(libs.kotlinx.datetime)

            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            implementation(projects.core.domain)
            implementation(projects.core.system)
            implementation(projects.feature.chat.domain)

            implementation(projects.coreFeatures.maps.source)
        }

        commonTest.dependencies {
            implementation(projects.testUtilities)
        }
    }
}
//
//tasks.configureEach {
//    val variant =
//        Regex("ksp(\\w+)KotlinAndroid").matchEntire(name)?.groupValues[1] ?: return@configureEach
//    dependsOn(
//        "generateResourceAccessorsForAndroid$variant",
//        "generateResourceAccessorsForAndroidMain",
//        "generateActualResourceCollectorsForAndroidMain",
//        "generateComposeResClass",
//        "generateResourceAccessorsForCommonMain",
//        "generateExpectResourceCollectorsForCommonMain"
//    )
//}

//afterEvaluate {
//    tasks.named("kspDebugKotlinAndroid") {
//        dependsOn(
//            "generateResourceAccessorsForAndroidDebug",
//            "generateResourceAccessorsForAndroidMain",
//            "generateActualResourceCollectorsForAndroidMain",
//            "generateComposeResClass",
//            "generateResourceAccessorsForCommonMain",
//            "generateExpectResourceCollectorsForCommonMain"
//        )
//    }
//
//    tasks.named("kspReleaseKotlinAndroid") {
//        dependsOn(
//            "generateResourceAccessorsForAndroidRelease",
//            "generateResourceAccessorsForAndroidMain",
//            "generateActualResourceCollectorsForAndroidMain",
//            "generateComposeResClass",
//            "generateResourceAccessorsForCommonMain",
//            "generateExpectResourceCollectorsForCommonMain"
//        )
//    }
//}
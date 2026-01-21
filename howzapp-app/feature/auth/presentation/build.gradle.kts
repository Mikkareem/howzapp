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
            implementation(libs.compose.components.resources)

            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)
            implementation(projects.feature.auth.api)
            implementation(projects.feature.auth.domain)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

//tasks.configureEach {
// "ksp(\\w+?)?(UnitTest|AndroidTest|Test)?Kotlin(Android|Desktop)"
//    val variant =
//        Regex("ksp(\\w+)KotlinAndroid").matchEntire(name)?.groupValues[1] ?: return@configureEach
//    dependsOn(
//        "generateResourceAccessorsForAndroid$variant",
//        "generateResourceAccessorsForAndroidMain",
//        "generateActualResourceCollectorsForAndroidMain",
//    )

//    val pattern = "ksp(\\w+?)?(UnitTest|AndroidTest|Test)?Kotlin(Android|Desktop)"
//
//    Regex(pattern).matchEntire(name)
//        ?.groupValues
//        ?.let {
//            val variant = it[1]
//            val type = it[2]
//            val target = it[3]
//
//            if(target == "Android") {
//                val dependencies = listOf(
//                    "generateResourceAccessorsForAndroidMain",
//                    "generateActualResourceCollectorsForAndroidMain"
//                )
//
//                val dependsOn1 = (dependencies + when(type) {
//                    "UnitTest" -> "generateResourceAccessorsForAndroid$type$variant"
//                    "AndroidTest" -> "generateResourceAccessorsForAndroid${type.replace("Android", "Instrumented")}$variant"
//                    else -> null
//                }).filterNotNull()
//
//                dependsOn(*dependsOn1.toTypedArray())
//            }
//        }
//}
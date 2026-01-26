plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKmpLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.composeStabilityAnalyzer) apply false

    alias(applicationLibs.plugins.conventions.android.application) apply false
    alias(applicationLibs.plugins.conventions.android.application.compose) apply false
    alias(applicationLibs.plugins.conventions.kmp.library) apply false
    alias(applicationLibs.plugins.conventions.cmp.library) apply false
    alias(applicationLibs.plugins.conventions.room) apply false
    alias(applicationLibs.plugins.conventions.koin.compiler) apply false
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradlePlugin)
    }
}

subprojects {
    plugins.withId("com.google.devtools.ksp") {
        plugins.withId("org.jetbrains.compose") {
            val isComposeResourceTaskAvailable =
                tasks.find { it.name.contains("generateResourceAccessors") } != null
            if (isComposeResourceTaskAvailable) {
                tasks.configureEach {
                    val pattern = "ksp(\\w+?)?(UnitTest|AndroidTest|Test)?Kotlin(Android|Desktop)"

                    Regex(pattern).matchEntire(name)
                        ?.groupValues
                        ?.let {
                            val variant = it[1]
                            val type = it[2]
                            val target = it[3]

                            val dependencies = listOf(
                                "generateResourceAccessorsFor${target}Main",
                                "generateActualResourceCollectorsFor${target}Main"
                            )

                            val additionalDependencies = if (target == "Android") {
                                // DemoDebug = Demo, Debug
                                val variantSplittedOnes = Regex("[A-Z][a-z]*").findAll(variant)
                                    .toList()
                                    .map { r ->
                                        "generateResourceAccessorsForAndroid${r.value}"
                                    }

                                variantSplittedOnes + when (type) {
                                    "AndroidTest" -> "generateResourceAccessorsForAndroid${
                                        type.replace(
                                            "Android",
                                            "Instrumented"
                                        )
                                    }$variant"

                                    else -> "generateResourceAccessorsForAndroid$type$variant"
                                }
                            } else emptyList()

                            dependsOn(
                                *(dependencies + additionalDependencies)
                                    .mapNotNull { tasks.findByName(it) }
                                    .toTypedArray()
                            )
                        }
                }
            }
        }
    }
}
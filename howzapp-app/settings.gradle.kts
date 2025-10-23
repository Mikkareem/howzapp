rootProject.name = "Howzapp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }

    versionCatalogs {
        create("applicationLibs") {
            from(files("gradle/application.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":test-utilities")
include(":composeApp")

include(":core:di")
include(":core:domain")
include(":core:data")
include(":core:designsystem")

include(":feature:chat:api")
include(":feature:chat:domain")
include(":feature:chat:database")
include(":feature:chat:data")
include(":feature:chat:presentation")
include(":feature:chat:test")

include(":feature:auth:api")
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:auth:presentation")
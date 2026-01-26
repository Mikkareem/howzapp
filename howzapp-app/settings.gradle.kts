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

include(":app:androidApp")
include(":app:shared")

include(":orchestration:database")
//include(":orchestration:navigation")
include(":orchestration:di")

include(":common:models")
include(":common:dto")
include(":common:utils")

include(":core:data:api")
include(":core:data:impl")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:session")
include(":core:system")
include(":core:di")
include(":core:designsystem")

//include(":core-features:media:api:data")
//include(":core-features:media:impl:data")
//
//include(":core-features:maps:impl")
//include(":core-features:maps:data")
//include(":core-features:maps:ui")

include(":features:auth:api:data")
include(":features:auth:api:ui")
include(":features:auth:data")
include(":features:auth:domain")
include(":features:auth:di")
include(":features:auth:models")
include(":features:auth:presentation")

include(":features:chats:api:data")
include(":features:chats:api:ui")
include(":features:chats:data")
include(":features:chats:database")
include(":features:chats:domain")
include(":features:chats:di")
include(":features:chats:models")
include(":features:chats:presentation")

//include(":features:common:database")

//include(":test-utilities")
//include(":android-benchmark")
//include(":android-baselineprofile")
//include(":android-instrumented")

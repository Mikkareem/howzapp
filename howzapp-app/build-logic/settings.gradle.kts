rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    versionCatalogs {
        create("applicationLibs") {
            from(files("../gradle/application.versions.toml"))
        }
    }
}

include(":conventions")
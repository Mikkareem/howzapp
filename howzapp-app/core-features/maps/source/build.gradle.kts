import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    alias(applicationLibs.plugins.conventions.cmp.library)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.maps.compose)
        }

        commonMain.dependencies {
            implementation(libs.ktor.http)

            implementation(libs.coil.compose)
            implementation(libs.coil.okhttp)

            implementation(projects.coreFeatures.maps.api)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

val properties = Properties()
File("secrets.properties").inputStream().use {
    properties.load(it)
}

buildkonfig {
    packageName = "com.techullurgy.howzapp.maps"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "MAPS_API_KEY",
            properties["MAPS_API_KEY"]?.toString() ?: ""
        )
    }
}
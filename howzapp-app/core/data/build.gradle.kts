import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)

            implementation(libs.bundles.ktor.common)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kermit)

            implementation(projects.core.domain)
            implementation(projects.core.internal)
        }
    }
}

buildkonfig {
    packageName = "com.techullurgy.howzapp.core.data"
    objectName = "HowzappKonfig"

    val localProperties = Properties().apply {
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) {
            load(propsFile.inputStream())
        }
    }

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "SERVER_HOST",
            localProperties["DESKTOP_SERVER_HOST"]?.toString() ?: "localhost",
            const = true
        )

        buildConfigField(
            FieldSpec.Type.INT,
            "SERVER_PORT",
            localProperties["SERVER_PORT"]?.toString() ?: "8080",
            const = true
        )
    }

    targetConfigs {
        create("android") {
            buildConfigField(
                FieldSpec.Type.STRING,
                "SERVER_HOST",
                localProperties["ANDROID_SERVER_HOST"]?.toString() ?: "10.0.2.2",
                const = true
            )
        }
    }
}
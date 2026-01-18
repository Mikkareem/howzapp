import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.TestOptions
import com.techullurgy.howzapp.conventions.applicationId
import com.techullurgy.howzapp.conventions.applicationLibs
import com.techullurgy.howzapp.conventions.configureKotlinAndroid
import com.techullurgy.howzapp.conventions.libs
import com.techullurgy.howzapp.conventions.versionCode
import com.techullurgy.howzapp.conventions.versionName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.konan.file.use
import java.util.Properties

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidApplication").get().get().pluginId)
            }

            extensions.configure<ApplicationExtension> {
                namespace = this@with.applicationId

                defaultConfig {
                    applicationId = this@with.applicationId

                    targetSdk {
                        version = release(applicationLibs.findVersion("android-targetSdk").get().toString().toInt())
                    }

                    versionCode = this@with.versionCode
                    versionName = this@with.versionName

                    testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
                    // testInstrumentationRunner = "com.techullurgy.howzapp.test.utilities.TestApplicationRunner"
                }

                signingConfigs {
                    create("release") {
                        val signingProperties = Properties().apply {
                            val signingPropertiesFile = rootProject.file("signing.properties")
                            if (signingPropertiesFile.exists()) {
                                signingPropertiesFile.reader().use { load(it) }
                            } else {
                                // Handle the case where the file is missing (e.g., for debug builds or CI/CD)
                                println("Warning: signing.properties file not found at ${signingPropertiesFile.absolutePath}")
                            }
                        }

                        storeFile =
                            signingProperties.getProperty("KEYSTORE_FILEPATH")?.let { file(it) }
                        storePassword = signingProperties.getProperty("KEYSTORE_PASSWORD")
                        keyAlias = signingProperties.getProperty("KEYSTORE_KEY_ALIAS")
                        keyPassword = signingProperties.getProperty("KEYSTORE_KEY_ALIAS_PASSWORD")
                    }
                }

                packaging {
                    resources {
                        excludes += setOf(
                            "/META-INF/{AL2.0,LGPL2.1}",
                        )
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        signingConfig = signingConfigs.getByName("release")
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                testOptions {
                    configureManagedDevices()
                }

                configureKotlinAndroid(this)
            }
        }
    }
}

private fun TestOptions.configureManagedDevices() {
    managedDevices {
        localDevices {
            create("pixel6") {
                apiLevel = 35
                device = "Pixel 6"
                systemImageSource = "aosp"
            }
        }
    }
}
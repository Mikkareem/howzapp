import com.android.build.api.dsl.ApplicationExtension
import com.techullurgy.howzapp.conventions.applicationId
import com.techullurgy.howzapp.conventions.applicationLibs
import com.techullurgy.howzapp.conventions.configureKotlinAndroid
import com.techullurgy.howzapp.conventions.versionCode
import com.techullurgy.howzapp.conventions.versionName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                namespace = this@with.applicationId

                defaultConfig {
                    applicationId = this@with.applicationId
                    targetSdk = applicationLibs.findVersion("android-targetSdk").get().toString().toInt()
                    versionCode = this@with.versionCode
                    versionName = this@with.versionName

                    testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
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
                        isMinifyEnabled = false
                    }
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
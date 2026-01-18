import com.techullurgy.howzapp.conventions.configureKotlinMultiplatform
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidKmpLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform()

                sourceSets.commonMain.dependencies {
                    implementation(target.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("kotlinx-serialization-json").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                }

                sourceSets.commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("assertk").get())
                }
            }
        }
    }
}
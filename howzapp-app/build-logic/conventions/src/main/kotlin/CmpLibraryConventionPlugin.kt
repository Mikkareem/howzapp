import com.techullurgy.howzapp.conventions.applicationLibs
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(applicationLibs.findPlugin("conventions-kmp-library").get().get().pluginId)
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
                apply(libs.findPlugin("composeStabilityAnalyzer").get().get().pluginId)
            }

//            val composeDependencies = extensions.getByType<ComposeExtension>().dependencies

            extensions.configure<KotlinMultiplatformExtension>() {
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("compose-runtime").get())

                    implementation(libs.findLibrary("compose-ui-tooling-preview").get())
                }
            }

            dependencies {
                "androidRuntimeClasspath"(libs.findLibrary("compose-ui-tooling").get())
            }
        }
    }
}
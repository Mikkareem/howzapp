import com.techullurgy.howzapp.conventions.applicationLibs
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

class CmpLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(applicationLibs.findPlugin("conventions-kmp-library").get().get().pluginId)
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            }

            val composeDependencies = extensions.getByType<ComposeExtension>().dependencies

            dependencies {
                "commonMainImplementation"(libs.findLibrary("compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("compose-foundation").get())
                "commonMainImplementation"(composeDependencies.material3)
                "commonMainImplementation"(libs.findLibrary("compose-ui-tooling-preview").get())
            }
        }
    }
}
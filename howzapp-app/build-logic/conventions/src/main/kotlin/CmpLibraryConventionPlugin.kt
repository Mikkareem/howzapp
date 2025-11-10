import com.techullurgy.howzapp.conventions.applicationLibs
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
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            val composeDependencies = extensions.getByType<ComposeExtension>().dependencies

            dependencies {
                "commonMainImplementation"(composeDependencies.ui)
                "commonMainImplementation"(composeDependencies.foundation)
                "commonMainImplementation"(composeDependencies.material3)
                "debugImplementation"(composeDependencies.uiTooling)

                "commonMainImplementation"(composeDependencies.components.uiToolingPreview)
                "androidMainImplementation"(composeDependencies.preview)
            }
        }
    }
}
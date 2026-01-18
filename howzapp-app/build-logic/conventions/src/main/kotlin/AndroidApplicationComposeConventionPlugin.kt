import com.techullurgy.howzapp.conventions.applicationLibs
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(applicationLibs.findPlugin("conventions-android-application").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            }
        }
    }
}
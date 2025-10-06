import com.techullurgy.howzapp.conventions.isAndroidEnabled
import com.techullurgy.howzapp.conventions.isDesktopEnabled
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinAnnotationsConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            dependencies {

                "commonMainImplementation"(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                "commonMainImplementation"(libs.findLibrary("koin-core").get())
                "commonMainImplementation"(libs.findLibrary("koin-annotations").get())


                add("kspCommonMainMetadata",libs.findLibrary("koin-ksp-compiler").get())
                if(isAndroidEnabled) {
                    add("kspAndroid",libs.findLibrary("koin-ksp-compiler").get())
                }
                if(isDesktopEnabled) {
                    add("kspDesktop",libs.findLibrary("koin-ksp-compiler").get())
                }
            }

            // Trigger Common Metadata Generation from Native tasks
            tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
                dependsOn("kspCommonMainKotlinMetadata")
            }
        }
    }
}
import com.techullurgy.howzapp.conventions.applicationId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpFeatureConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("$applicationId.conventions.cmp.library")
            }

            dependencies {
//                "commonMainImplementation"(project(":core:presentation"))
//                "commonMainImplementation"(project(":core:designsystem"))
//
//                "commonMainImplementation"(platform(libs.findLibrary("koin-bom").get()))
//                if(isAndroidEnabled) {
//                    "androidMainImplementation"(platform(libs.findLibrary("koin-bom").get()))
//                }
//
//                "commonMainImplementation"(libs.findLibrary("koin-compose").get())
//                "commonMainImplementation"(libs.findLibrary("koin-compose-viewmodel").get())
//
//                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-viewmodel").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-compose").get())
//
//                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel-savedstate").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-savedstate").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-bundle").get())
//                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-navigation").get())
//
//                if(isAndroidEnabled) {
//                    "androidMainImplementation"(libs.findLibrary("koin-android").get())
//                    "androidMainImplementation"(libs.findLibrary("koin-androidx-compose").get())
//                    "androidMainImplementation"(libs.findLibrary("koin-androidx-navigation").get())
//                    "androidMainImplementation"(libs.findLibrary("koin-core-viewmodel").get())
//                }
            }
        }
    }
}
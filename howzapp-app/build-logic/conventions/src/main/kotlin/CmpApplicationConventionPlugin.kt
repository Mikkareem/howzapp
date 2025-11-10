import com.techullurgy.howzapp.conventions.applyHierarchyTemplate
import com.techullurgy.howzapp.conventions.configureAndroidTarget
import com.techullurgy.howzapp.conventions.configureDesktopTarget
import com.techullurgy.howzapp.conventions.configureIosTargets
import com.techullurgy.howzapp.conventions.isAndroidEnabled
import com.techullurgy.howzapp.conventions.isDesktopEnabled
import com.techullurgy.howzapp.conventions.isHierarchyEnabled
import com.techullurgy.howzapp.conventions.isIosEnabled
import com.techullurgy.howzapp.conventions.libs
import com.techullurgy.howzapp.conventions.applicationId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("$applicationId.conventions.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            if(isAndroidEnabled) {
                configureAndroidTarget()
            }
            if (isIosEnabled) {
                configureIosTargets()
            }
            if (isDesktopEnabled) {
                configureDesktopTarget()
            }

            extensions.configure<KotlinMultiplatformExtension> {
                if (isHierarchyEnabled) {
                    applyHierarchyTemplate()
                }
            }

//            dependencies {
//                "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
//            }
        }
    }
}
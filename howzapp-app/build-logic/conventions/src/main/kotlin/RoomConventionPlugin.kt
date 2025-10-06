import androidx.room.gradle.RoomExtension
import com.techullurgy.howzapp.conventions.isAndroidEnabled
import com.techullurgy.howzapp.conventions.isDesktopEnabled
import com.techullurgy.howzapp.conventions.isIosEnabled
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("room").get().get().pluginId)
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "commonMainApi"(libs.findLibrary("androidx-room-runtime").get())
                "commonMainApi"(libs.findLibrary("sqlite-bundled").get())

                add("kspCommonMainMetadata", libs.findLibrary("androidx-room-compiler").get())
                if(isAndroidEnabled) {
                    add("kspAndroid", libs.findLibrary("androidx-room-compiler").get())
                }
                if(isIosEnabled) {
                    add("kspIosSimulatorArm64",libs.findLibrary("androidx-room-compiler").get())
                    add("kspIosArm64",libs.findLibrary("androidx-room-compiler").get())
                    add("kspIosX64",libs.findLibrary("androidx-room-compiler").get())
                }
                if(isDesktopEnabled) {
                    add("kspDesktop",libs.findLibrary("androidx-room-compiler").get())
                }
            }
        }
    }
}
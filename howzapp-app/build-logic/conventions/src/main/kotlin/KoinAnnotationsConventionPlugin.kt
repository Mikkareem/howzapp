import com.android.build.api.dsl.CommonExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.techullurgy.howzapp.conventions.isAndroidEnabled
import com.techullurgy.howzapp.conventions.isDesktopEnabled
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinAnnotationsConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            extensions.findByType<KotlinMultiplatformExtension>()?.run {
                sourceSets.commonMain.dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("koin-annotations").get())
                }

                this@with.dependencies {
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
            } ?: run {
                dependencies {
                    "implementation"(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    "implementation"(libs.findLibrary("koin-core").get())
                    "implementation"(libs.findLibrary("koin-annotations").get())
                }
            }

            extensions.getByType<KspExtension>().arg("KOIN_CONFIG_CHECK", "true")
        }
    }
}
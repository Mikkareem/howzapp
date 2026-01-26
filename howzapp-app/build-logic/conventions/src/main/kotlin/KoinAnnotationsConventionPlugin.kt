import com.google.devtools.ksp.gradle.KspAATask
import com.techullurgy.howzapp.conventions.isAndroidEnabled
import com.techullurgy.howzapp.conventions.isIosEnabled
import com.techullurgy.howzapp.conventions.isJvmEnabled
import com.techullurgy.howzapp.conventions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
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
                    if (isIosEnabled) {
                        add("kspIosSimulatorArm64", libs.findLibrary("koin-ksp-compiler").get())
                        add("kspIosArm64", libs.findLibrary("koin-ksp-compiler").get())
                        add("kspIosX64", libs.findLibrary("koin-ksp-compiler").get())
                    }
                    if (isJvmEnabled) {
                        add("kspJvm", libs.findLibrary("koin-ksp-compiler").get())
                    }
                }

                // Generate CommonMain Metadata for Koin
                tasks.withType<KspAATask>().configureEach {
                    if (name != "kspCommonMainKotlinMetadata") {
                        dependsOn("kspCommonMainKotlinMetadata")
                    }
                }

                sourceSets.commonMain.configure {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }
            } ?: run {
                dependencies {
                    "implementation"(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    "implementation"(libs.findLibrary("koin-core").get())
                    "implementation"(libs.findLibrary("koin-annotations").get())
                }
            }

//            extensions.getByType<KspExtension>().arg("KOIN_CONFIG_CHECK", "true")
        }
    }
}
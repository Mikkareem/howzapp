package com.techullurgy.howzapp.conventions

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.applicationLibs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("applicationLibs")

val Project.isAndroidEnabled: Boolean get() = true
val Project.isIosEnabled: Boolean get() = false
val Project.isJvmEnabled: Boolean get() = true
val Project.isHierarchyEnabled: Boolean get() = false

val Project.applicationId: String get() = applicationLibs.findVersion("projectApplicationId").get().toString()
val Project.versionCode: Int get() = applicationLibs.findVersion("projectVersionCode").get().toString().toInt()
val Project.versionName: String get() = applicationLibs.findVersion("projectVersionName").get().toString()

private val Project.javaVersionString get() = applicationLibs.findVersion("javaVersion").get().toString()

val Project.javaVersion: JavaVersion get() = JavaVersion.toVersion(javaVersionString.toInt())
val Project.jvmTarget: JvmTarget get() = JvmTarget.fromTarget(javaVersionString)
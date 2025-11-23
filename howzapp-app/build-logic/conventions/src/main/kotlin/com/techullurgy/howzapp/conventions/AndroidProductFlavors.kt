package com.techullurgy.howzapp.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
internal enum class FlavorDimension {
    contentType
}

@Suppress("EnumEntryName")
enum class HowzappFlavor(
    internal val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val appName: String? = null
) {
    demo(FlavorDimension.contentType, ".demo", "Demo"),
    prod(FlavorDimension.contentType)
}

internal fun CommonExtension<*, *, *, *, *, *>.configureAndroidProductFlavors(
    configure: ProductFlavor.(HowzappFlavor) -> Unit = {}
) {
    apply {
        FlavorDimension.entries.forEach { dimension ->
            flavorDimensions += dimension.name
        }

        productFlavors {
            HowzappFlavor.entries.forEach { flavor ->
                register(flavor.name) {
                    dimension = flavor.dimension.name
                    configure(flavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (flavor.applicationIdSuffix != null) {
                            applicationIdSuffix = flavor.applicationIdSuffix
                        }
                        if (flavor.appName != null) {
                            manifestPlaceholders.put("appLabel", " (${flavor.appName})")
                        } else {
                            manifestPlaceholders.put("appLabel", "")
                        }
                    }
                }
            }
        }
    }
}
-dontwarn org.apiguardian.api.API

# Keep annotation definitions
-keep class org.koin.core.annotation.** { *; }

# Keep classes annotated with Koin annotations
-keep @org.koin.core.annotation.* class * { *; }

-keepnames @kotlinx.serialization.Serializable class * { *; }

# Keep Kotlinx Serialization generated classes
-keepclassmembers class * implements kotlinx.serialization.KSerializer { *;}

# Keep serializer for all @Serializable classes
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static ** serializer(...);
}
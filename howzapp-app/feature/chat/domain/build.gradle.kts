plugins {
    alias(applicationLibs.plugins.conventions.kmp.library)
    alias(applicationLibs.plugins.conventions.koin.compiler)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}
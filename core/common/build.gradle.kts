plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("droidkaigi.primitive.kmp")
    alias(libs.plugins.metro)
}

kotlin {
    iosX64()
}

dependencies {
    commonMainImplementation(projects.core.model)
    commonMainImplementation(compose.runtime)
    commonMainImplementation(libs.rin)
}

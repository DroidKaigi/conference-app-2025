plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.spotless")
    alias(libs.plugins.metro)
}

dependencies {
    commonMainImplementation(projects.core.model)
    commonMainImplementation(compose.runtime)
    commonMainImplementation(libs.rin)
    androidMainImplementation(libs.androidxXrCompose)
}

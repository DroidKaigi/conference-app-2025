plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.aboutlibraries")
}

val aboutLibrariesTargetDir = "${layout.buildDirectory.get().asFile.path}/generated/aboutlibraries"

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        main {
            resources.srcDir(aboutLibrariesTargetDir)
        }
    }
}

compose.desktop.application.mainClass = "io.github.droidkaigi.confsched.MainKt"

dependencies {
    implementation(projects.appShared)
    implementation(projects.core.data)
    implementation(compose.desktop.currentOs)
    implementation(compose.foundation)
    implementation(compose.runtime)
    implementation(libs.navigationeventDesktop)
    implementation(libs.lifecycleRuntimeCompose)
    implementation(compose.ui)
    implementation(compose.uiUtil)
}

aboutLibraries.export {
    outputFile.set(file("${aboutLibrariesTargetDir}/licenses.json"))
}

tasks.named("processResources") {
    dependsOn("exportLibraryDefinitions")
}

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.aboutlibraries")
    id("droidkaigi.primitive.spotless")
    id("droidkaigi.primitive.kmp.compose.resources")
}

compose {
    resources {
        nameOfResClass = "AppDesktopRes"
    }
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

compose.desktop {
    application {
        nativeDistributions {
            // For display name of “app-desktop.app” in Finder/Dock/DMG
            packageName = "DroidKaigi 2025"
            packageVersion = libs.versions.droidkaigiApp.get()
            copyright = "Copyright 2025 DroidKaigi committee"
            licenseFile.set(rootProject.file("LICENSE.md"))

            // Prevent a runtime error from occurring with java.lang.ClassNotFoundException: sun.misc.Unsafe.
            // For distribution files such as dmg, exe, and msi, the “minimal JRE” is used, which does not include sun.misc.Unsafe for compatibility reasons.
            // When running with the “run” command, it is executed using the “Full JDK (development version),” which includes sun.misc.Unsafe, so the ClassNotFoundException does not occur.
            // https://github.com/DroidKaigi/conference-app-2025/issues/322
            modules("jdk.unsupported")

            targetFormats(
                // for MacOS
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                // for Windows
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe,
                // for Linux
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Rpm
            )

            macOS {
                bundleID = macOS.bundleID
                iconFile.set(project.file("src/main/resources/DroidKaigi2025.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/DroidKaigi2025.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/ic_app_512.png"))
            }
        }
    }
}

/**
 * Ensures that `exportLibraryDefinitions` runs before key desktop build tasks.
 *
 * The following tasks are covered:
 *
 * - **processResources** — Resource processing task for the JVM target.
 * - **createDistributable** — Creates the base distributable package for desktop apps.
 * - **packageDmg** — Builds a macOS DMG installer.
 * - **packageMsi** — Builds a Windows MSI installer.
 * - **packageExe** — Builds a Windows standalone EXE.
 * - **packageDeb** — Builds a Linux `.deb` package.
 * - **packageRpm** — Builds a Linux `.rpm` package.
 *
 * By declaring `dependsOn("exportLibraryDefinitions")`, these tasks will always run
 * the license export step first, ensuring `licenses.json` is generated and included
 * in the build outputs.
 */
listOf(
    "processResources",
    "createDistributable",
    "packageDmg",
    "packageMsi", "packageExe",
    "packageDeb", "packageRpm"
).forEach { n ->
    tasks.matching { it.name == n }.configureEach {
        dependsOn("exportLibraryDefinitions")
    }
}

/**
 * Adds a JVM system property (`-Dapp.devRun=true`) when the application is launched
 * via Gradle tasks `:run` or `:runDistributable`.
 *
 * This allows the application code to detect that it is running in a **development context**
 * (e.g., `./gradlew :app-desktop:run`) and apply behavior that should not be included
 * in packaged distributions, such as dynamically overriding the Dock/Taskbar icon.
 *
 * Packaged applications (DMG/MSI/EXE/etc.) will not have this flag set,
 * ensuring that runtime-only features are limited to development runs.
 */
tasks.withType(JavaExec::class).matching {
    it.name in listOf(
        "run", "runDistributable",
        "hotDev", "hotDevAsync", "hotRun", "hotRunAsync", "runHot"
    )
}.configureEach {
    jvmArgs("-Dapp.devRun=true")
    // Just to be safe, we will add the jdk.unsupported module during development execution. (To prevent recurrence due to vendor JDK differences)
    jvmArgs("--add-modules=jdk.unsupported")
}

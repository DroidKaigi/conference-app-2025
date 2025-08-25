plugins {
    alias(libs.plugins.kotlinMultiplatform)
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
    jvm("desktop")

    // JDK Version 21 is required to create the distribution materials.
    jvmToolchain(21)

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.appShared)
                implementation(projects.core.data)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.navigationeventDesktop)
                implementation(libs.lifecycleRuntimeCompose)
                implementation(compose.uiUtil)
            }
            resources.srcDir(aboutLibrariesTargetDir)
        }
    }
}

compose.desktop.application.mainClass = "io.github.droidkaigi.confsched.MainKt"

aboutLibraries.export {
    outputFile.set(file("${aboutLibrariesTargetDir}/licenses.json"))
}

compose.desktop {
    application {
        nativeDistributions {
            // For display name of “app-desktop.app” in Finder/Dock/DMG
            packageName = "DroidKaigi 2025"
            // If necessary, modify the code to obtain and load packageVersion from a single source on all platforms.
            packageVersion = "1.0.0"

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
                iconFile.set(project.file("src/desktopMain/resources/DroidKaigi2025.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/DroidKaigi2025.ico"))
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/ic_app_512.png"))
            }
        }
    }
}

/**
 * Ensures that `exportLibraryDefinitions` runs before key desktop build tasks.
 *
 * The following tasks are covered:
 *
 * - **desktopProcessResources** — Resource processing task for the KMP `jvm("desktop")` target.
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
    "desktopProcessResources",
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
    it.name == "run" || it.name == "runDistributable"
}.configureEach {
    jvmArgs("-Dapp.devRun=true")
}

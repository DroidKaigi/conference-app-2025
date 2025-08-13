import org.jetbrains.compose.ComposePlugin.CommonComponentsDependencies
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import util.Arch
import util.activeArch
import util.androidJvmMain

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.skie")
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.compose.resources")
    id("droidkaigi.primitive.buildkonfig")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("droidkaigi.primitive.aboutlibraries")
}

compose.resources.nameOfResClass = "AppSharedRes"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.droidkaigiui)
            implementation(projects.core.model)
            implementation(projects.core.designsystem)
            implementation(projects.feature.sessions)
            implementation(projects.feature.eventmap)
            implementation(projects.feature.about)
            implementation(projects.feature.sponsors)
            implementation(projects.feature.settings)
            implementation(projects.feature.staff)
            implementation(projects.feature.contributors)
            implementation(projects.feature.favorites)
            implementation(projects.feature.profile)

            implementation(libs.kotlinxSerializationJson)
            implementation(libs.rin)
            implementation(libs.soilQueryCompose)
            implementation(libs.androidxDatastorePreferencesCore)
            implementation(libs.haze)
        }

        androidMain.dependencies {
            implementation(libs.lifecycleViewmodelNavigation3)
            implementation(libs.androidxActivityCompose)
            implementation(libs.navigation3Adaptive)
            implementation(libs.androidxBrowser)
        }

        androidJvmMain.dependencies {
            implementation(libs.navigation3Ui.get().toString()) {
                /**
                 * Exclude androidx.compose.ui to avoid runtime crash on JVM:
                 * ```
                 * kotlin.NotImplementedError: Implemented only in JetBrains fork.
                 * Please use `org.jetbrains.compose.ui:ui-util` package instead.
                 * ```
                 *
                 * navigation3Ui includes non-multiplatform code and is not officially supported on JVM.
                 * This exclusion is a workaround to prevent the crash.
                 */
                exclude(group = "androidx.compose.ui")
            }
            implementation(libs.navigation3Runtime)
        }

        // Since we manually configured `dependsOn` for androidJvmMain,
        // the default source set hierarchy is disabled.
        // Therefore, we need to manually set up iosMain and link each iOS target to it.
        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.navigationCompose)
                implementation(libs.ktorClientDarwin)
                implementation(libs.molecule)
                api(projects.feature.sessions)
                api(projects.feature.contributors)
                api(projects.core.model)
                api(projects.core.data)
            }
        }

        listOf(
            iosArm64Main.get(),
            iosX64Main.get(),
            iosSimulatorArm64Main.get(),
        ).forEach { it ->
            it.dependsOn(iosMain)
        }
    }

    val frameworkName = "shared"
    val xcf = XCFramework(frameworkName)
    targets.filterIsInstance<KotlinNativeTarget>()
        .forEach {
            it.binaries {
                framework {
                    baseName = frameworkName
                    // compose for iOS(skiko) needs to be static library
                    isStatic = true

                    binaryOption("bundleId", "io.github.droidkaigi.confsched.shared")
                    binaryOption("bundleVersion", version.toString())
                    binaryOption("bundleShortVersionString", version.toString())

                    val includeToXCF = when (project.activeArch) {
                        Arch.ARM -> {
                            this.target.name.contains("iosArm64") || this.target.name.contains("iosSimulatorArm64")
                        }

                        Arch.ARM_SIMULATOR_DEBUG -> {
                            this.target.name.contains("iosSimulatorArm64") && this.debuggable && !this.optimized
                        }

                        Arch.X86 -> {
                            this.target.name.contains("iosX64")
                        }

                        Arch.ALL -> {
                            true
                        }
                    }

                    if (includeToXCF) {
                        xcf.add(this)
                        logger.lifecycle("framework '${this.name} ${this.target}' will be in XCFramework")
                    }

                    export(projects.feature.sessions)
                    export(projects.feature.contributors)
                    export(projects.core.model)
                    export(projects.core.data)
                    export(CommonComponentsDependencies.resources)
                }
            }
        }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// generate aboutlibraries.json for iOS
tasks.named("prepareComposeResourcesTaskForIosMain").get().dependsOn("exportLibraryDefinitions")
tasks.named("copyNonXmlValueResourcesForIosMain").get().dependsOn("exportLibraryDefinitions")
aboutLibraries.export {
    outputFile = file("src/iosMain/composeResources/files/aboutlibraries.json")
    exportVariant = "metadataIosMain"
}

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.metro")
    id("droidkaigi.primitive.spotless")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=androidx.compose.ui.test.ExperimentalTestApi",
        "-opt-in=soil.query.annotation.ExperimentalSoilQueryApi",
    )

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.droidkaigiui)
            api(projects.core.model)
            implementation(projects.core.common)
            implementation(projects.core.droidkaigiui)
            implementation(projects.core.designsystem)

            implementation(projects.core.data)
            implementation(projects.feature.sessions)
            implementation(projects.feature.about)
            implementation(projects.feature.contributors)
            implementation(projects.feature.eventmap)
            implementation(projects.feature.staff)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(compose.runtime)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)

            implementation(libs.soilQueryCompose)
            implementation(libs.soilQueryCore)
            implementation(libs.lifecycleViewmodelCompose)
            implementation(libs.lifecycleRuntimeCompose)
            implementation(libs.androidxDatastorePreferencesCore)
            implementation(libs.material3)
        }

        androidMain.dependencies {
            api(libs.robolectric)
            api(libs.androidxTestCore)
            api(libs.androidxActivityCompose)
            api(libs.rin)
            implementation(libs.roborazzi)
            implementation(libs.roborazziCompose)

            implementation(project.dependencies.platform(libs.composeBom))
            api(libs.androidxUiTestJunit4)
            implementation(libs.androidxUiTestManifest)

            implementation(libs.roborazziPreviewScannerSupport)
            implementation(libs.composablePreviewScannerAndroid)
            implementation(libs.composablePreviewScannerJvm)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinTestJunit)
            implementation(libs.roborazziComposeDesktop)
        }

        iosMain.dependencies {
            implementation(libs.roborazziComposeIos)
        }
    }
}

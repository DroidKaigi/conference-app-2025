package droidkaigi.primitive

import com.android.build.gradle.BaseExtension
import io.github.takahirom.roborazzi.RoborazziExtension
import util.getDefaultPackageName
import util.kotlin
import util.library
import util.libs

plugins {
    id("io.github.takahirom.roborazzi")
}

configure<RoborazziExtension> {
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf(getDefaultPackageName(project.name))
        includePrivatePreviews = true
        testerQualifiedClassName = "io.github.droidkaigi.confsched.testing.previewtester.DroidKaigiKmpPreviewTester"
        robolectricConfig = mapOf(
            // Roborazzi 1.46.1 uses SDK 33 by default, which causes compatibility issues with coil-ktor3
            // resulting in a JNI ClassNotFoundException on CI.
            // To avoid this, we explicitly set the SDK version to 35.
            // See https://github.com/coil-kt/coil/issues/3049 for details.
            "sdk" to "[35]",
            "qualifiers" to "RobolectricDeviceQualifiers.Pixel4a",
        )
    }
}

configure<BaseExtension> {
    testOptions.unitTests.all {
        it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
        it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
    }
}

kotlin {
    sourceSets {
        androidUnitTest {
            dependencies {
                implementation(libs.library("roborazziPreviewScannerSupport"))
                implementation(libs.library("composablePreviewScannerAndroid"))
                implementation(libs.library("junit"))
                implementation(libs.library("robolectric"))
            }
        }
    }
}

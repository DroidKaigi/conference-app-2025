package io.github.droidkaigi.confsched.testing.robot.core

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import com.github.takahirom.roborazzi.DefaultFileNameGenerator
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.captureRoboImage

@OptIn(InternalRoborazziApi::class)
context(composeUiTest: ComposeUiTest)
actual fun SemanticsNodeInteraction.captureNodeWithDescription(description: String) {
    val filePath = "android/" + DefaultFileNameGenerator.generateFilePath()
        .split(".")
        .dropLast(2) // drop method name and extension
        .joinToString(".")
        .plus(" - $description.png")
    println("Capture screen: $filePath")
    this.captureRoboImage(filePath)
}

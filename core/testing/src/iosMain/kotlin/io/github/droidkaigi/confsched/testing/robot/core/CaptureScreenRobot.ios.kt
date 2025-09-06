package io.github.droidkaigi.confsched.testing.robot.core

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import io.github.takahirom.roborazzi.captureRoboImage

@OptIn(ExperimentalRoborazziApi::class)
context(composeUiTest: ComposeUiTest)
actual fun SemanticsNodeInteraction.captureNodeWithDescription(description: String) {
    captureRoboImage(
        composeUiTest = composeUiTest,
        filePath = "$description - ios.png",
    )
}

package io.github.droidkaigi.confsched.testing.robot.core

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onNodeWithTag
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.testing.behavior.TestCaseDescriptionProvider
import io.github.droidkaigi.confsched.testing.compose.TestRootTestTag

interface CaptureScreenRobot {
    context(composeUiTest: ComposeUiTest, checkNode: TestCaseDescriptionProvider)
    fun captureScreenWithChecks(checks: () -> Unit)
}

context(composeUiTest: ComposeUiTest)
expect fun SemanticsNodeInteraction.captureNodeWithDescription(description: String)

@Inject
class DefaultCaptureScreenRobot : CaptureScreenRobot {
    context(composeUiTest: ComposeUiTest, testCaseDescriptionProvider: TestCaseDescriptionProvider)
    override fun captureScreenWithChecks(checks: () -> Unit) {
        checks()
        composeUiTest
            .onNodeWithTag(TestRootTestTag)
            .captureNodeWithDescription(testCaseDescriptionProvider.description)
    }
}

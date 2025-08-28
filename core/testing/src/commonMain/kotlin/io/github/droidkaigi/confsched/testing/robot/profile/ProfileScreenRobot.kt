package io.github.droidkaigi.confsched.testing.robot.profile

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import dev.zacsweers.metro.Inject
import io.github.confsched.profile.ProfileCardCardScreenTestTag
import io.github.confsched.profile.ProfileCardEditScreenColumnTestTag
import io.github.confsched.profile.ProfileScreenContext
import io.github.confsched.profile.ProfileScreenRoot
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class ProfileScreenRobot(
    private val screenContext: ProfileScreenContext,
    private val testDispatcher: TestDispatcher,
    captureScreenRobot: DefaultCaptureScreenRobot,
    dataScreenRobot: DefaultProfileDataStoreRobot,
    waitRobot: DefaultWaitRobot,
): CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot,
    ProfileDataStoreRobot by dataScreenRobot{

    context(composeUiTest: ComposeUiTest)
    fun setupProfileScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    ProfileScreenRoot(
                        onShareClick = { _, _ ->}
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEditScreenDisplayed() {
        composeUiTest.onNodeWithTag(ProfileCardEditScreenColumnTestTag).assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkCardScreenDisplayed() {
        composeUiTest.onNodeWithTag(ProfileCardCardScreenTestTag).assertExists()
    }
}

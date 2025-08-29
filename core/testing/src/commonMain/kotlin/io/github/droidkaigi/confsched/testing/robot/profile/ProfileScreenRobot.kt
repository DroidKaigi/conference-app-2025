package io.github.droidkaigi.confsched.testing.robot.profile

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.zacsweers.metro.Inject
import io.github.confsched.profile.ProfileCardCardScreenTestTag
import io.github.confsched.profile.ProfileCardEditCreateCardButton
import io.github.confsched.profile.ProfileCardEditLinkFormTestTag
import io.github.confsched.profile.ProfileCardEditNameErrorTextTestTag
import io.github.confsched.profile.ProfileCardEditNameFormTestTag
import io.github.confsched.profile.ProfileCardEditOccupationFormTestTag
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

    context(composeUiTest: ComposeUiTest)
    fun inputName(name: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditNameFormTestTag)
            .performTextInput(name)
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun inputOccupation(occupation: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditOccupationFormTestTag)
            .performTextInput(occupation)
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun inputLink(link: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditLinkFormTestTag)
            .performTextInput(link)
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkName(name: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditNameFormTestTag)
            .assertTextEquals(name)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOccupation(occupation: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditOccupationFormTestTag)
            .assertTextEquals(occupation)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkLink(link: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditLinkFormTestTag)
            .assertTextEquals(link)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickCreateButton() {
        composeUiTest
            .onNodeWithTag(ProfileCardEditCreateCardButton)
            .performClick()
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkNameError() {
        composeUiTest
            .onNodeWithTag(ProfileCardEditNameErrorTextTestTag)
            .assertIsDisplayed()
    }
}

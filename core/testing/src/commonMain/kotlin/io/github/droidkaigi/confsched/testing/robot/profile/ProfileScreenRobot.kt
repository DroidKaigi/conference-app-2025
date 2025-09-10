package io.github.droidkaigi.confsched.testing.robot.profile

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import dev.zacsweers.metro.Inject
import io.github.confsched.profile.ProfileCardCardScreenTestTag
import io.github.confsched.profile.ProfileCardEditButtonTestTag
import io.github.confsched.profile.ProfileCardEditCreateCardButtonTestTag
import io.github.confsched.profile.ProfileCardEditImageErrorTextTestTag
import io.github.confsched.profile.ProfileCardEditLinkErrorTextTestTag
import io.github.confsched.profile.ProfileCardEditLinkFormTestTag
import io.github.confsched.profile.ProfileCardEditNameErrorTextTestTag
import io.github.confsched.profile.ProfileCardEditNameFormTestTag
import io.github.confsched.profile.ProfileCardEditOccupationErrorTextTestTag
import io.github.confsched.profile.ProfileCardEditOccupationFormTestTag
import io.github.confsched.profile.ProfileCardEditScreenColumnTestTag
import io.github.confsched.profile.ProfileCardEditThemeTestTag
import io.github.confsched.profile.ProfileScreenContext
import io.github.confsched.profile.ProfileScreenRoot
import io.github.confsched.profile.components.ProfileCardFlipCardBackTestTag
import io.github.confsched.profile.components.ProfileCardFlipCardFrontTestTag
import io.github.confsched.profile.components.ProfileCardFlipCardTestTag
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
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
) : CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot,
    ProfileDataStoreRobot by dataScreenRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupProfileScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    ProfileScreenRoot(
                        onShareClick = { _, _ -> },
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
    fun checkCardFrontDisplayed() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardFlipCardFrontTestTag,
                useUnmergedTree = true,
            )
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkCardBackDisplayed() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardFlipCardBackTestTag,
                useUnmergedTree = true,
            )
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun flipProfileCard() {
        composeUiTest.mainClock.advanceTimeBy(1800) // wait for the initial animation to finish
        composeUiTest
            .onNodeWithTag(ProfileCardFlipCardTestTag)
            .assertIsEnabled()
            .performClick()
        composeUiTest.mainClock.advanceTimeBy(500)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickEditButton() {
        composeUiTest
            .onNodeWithTag(ProfileCardEditButtonTestTag)
            .performClick()
        waitUntilIdle()
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
            .onNodeWithTag(ProfileCardEditCreateCardButtonTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkThemeSelected(theme: ProfileCardTheme) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditThemeTestTag.plus(theme.name))
            .assertIsSelected()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickCardTheme(theme: ProfileCardTheme) {
        composeUiTest.onNodeWithTag(ProfileCardEditThemeTestTag.plus(theme.name))
            .assertExists()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkNameError() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardEditNameErrorTextTestTag,
                useUnmergedTree = true,
            )
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOccupationError() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardEditOccupationErrorTextTestTag,
                useUnmergedTree = true,
            )
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkLinkEmptyError() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardEditLinkErrorTextTestTag,
                useUnmergedTree = true,
            )
            .assertTextContains("Please enter a Link")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkLinkInvalidError() {
        composeUiTest
            .onNodeWithTag(
                testTag = ProfileCardEditLinkErrorTextTestTag,
                useUnmergedTree = true,
            )
            .assertTextContains("The URL format is incorrect")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkImageEmptyError() {
        composeUiTest
            .onNodeWithTag(ProfileCardEditImageErrorTextTestTag)
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToNodeTagInEditScreen(tag: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardEditScreenColumnTestTag)
            .performScrollToNode(hasTestTag(tag))
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToNodeTagInCardScreen(tag: String) {
        composeUiTest
            .onNodeWithTag(ProfileCardCardScreenTestTag)
            .performScrollToNode(hasTestTag(tag))
        waitFor5Seconds()
    }
}

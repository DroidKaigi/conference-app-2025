package io.github.droidkaigi.confsched.testing.robot.about

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.about.AboutScreenContext
import io.github.droidkaigi.confsched.about.AboutScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.about.AboutScreenRoot
import io.github.droidkaigi.confsched.about.section.AboutCreditsContributorsItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsSponsorsItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsStaffItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutCreditsTitleTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterMediumItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterXItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterYouTubeItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutHeaderTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersCodeOfConductItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersLicenseItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersPrivacyPolicyItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersSettingsItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutOthersTitleTestTag
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class AboutScreenRobot(
    private val screenContext: AboutScreenContext,
    private val testDispatcher: TestDispatcher,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupAboutScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    AboutScreenRoot(
                        onAboutItemClick = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToCreditsSection() {
        composeUiTest
            .onNode(hasTestTag(AboutScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(AboutCreditsTitleTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the credits section.
        composeUiTest.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 150)
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToOthersSection() {
        composeUiTest
            .onNode(hasTestTag(AboutScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(AboutOthersSettingsItemTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the credits section.
        composeUiTest.onRoot().performTouchInput {
            swipeDown(startY = centerY, endY = centerY + 100)
        }

        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToFooterSection() {
        composeUiTest
            .onNode(hasTestTag(AboutScreenLazyColumnTestTag))
            .performScrollToNode(hasTestTag(AboutFooterTestTag))
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkHeaderSectionDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutHeaderTestTag))
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkCreditsSectionTitleDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutCreditsTitleTestTag))
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkCreditsSectionContentsDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutCreditsContributorsItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutCreditsStaffItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutCreditsSponsorsItemTestTag))
            .assertExists()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOthersSectionTitleDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutOthersTitleTestTag))
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOthersSectionContentsDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutOthersCodeOfConductItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutOthersLicenseItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutOthersPrivacyPolicyItemTestTag))
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutOthersSettingsItemTestTag))
            .assertExists()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkFooterSectionDisplayed() {
        composeUiTest
            .onNode(hasTestTag(AboutFooterTestTag))
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutFooterYouTubeItemTestTag))
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutFooterXItemTestTag))
            .assertIsDisplayed()

        composeUiTest
            .onNode(hasTestTag(AboutFooterMediumItemTestTag))
            .assertIsDisplayed()
    }
}

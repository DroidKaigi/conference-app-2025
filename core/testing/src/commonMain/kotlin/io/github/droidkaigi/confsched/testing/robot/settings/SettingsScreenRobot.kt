package io.github.droidkaigi.confsched.testing.robot.settings

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performSemanticsAction
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.designsystem.theme.changoFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoMediumFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoRegularFontFamily
import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily
import io.github.droidkaigi.confsched.model.settings.Settings
import io.github.droidkaigi.confsched.settings.SettingsScreenContext
import io.github.droidkaigi.confsched.settings.SettingsScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.settings.SettingsScreenRoot
import io.github.droidkaigi.confsched.settings.component.SettingsAccessibilityUseFontFamilySettingsItemRowTestTag
import io.github.droidkaigi.confsched.settings.component.SettingsItemRowCurrentValueTextTestTag
import io.github.droidkaigi.confsched.settings.section.SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class SettingsScreenRobot(
    private val screenContext: SettingsScreenContext,
    private val testDispatcher: TestDispatcher,
    settingsDataStoreRoboto: DefaultSettingsDataStoreRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : SettingsDataStoreRobot by settingsDataStoreRoboto,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    enum class TestFontFamily(
        val displayName: String,
    ) {
        ChangoRegular(
            displayName = "Chango Regular",
        ),
        RobotoRegular(
            displayName = "Roboto Regular",
        ),
        RobotoMedium(
            displayName = "Roboto Medium",
        ),
        SystemDefault(
            displayName = "Default",
        ),
    }

    context(composeUiTest: ComposeUiTest)
    fun setupSettingsScreenContent() {
        composeUiTest.setContent {
            val settings by remember { getSettingsStream() }
                .collectAsState(
                    initial = Settings(
                        useKaigiFontFamily = KaigiFontFamily.ChangoRegular,
                    ),
                )

            val fontFamily = when (settings.useKaigiFontFamily) {
                KaigiFontFamily.ChangoRegular -> changoFontFamily()
                KaigiFontFamily.RobotoRegular -> robotoRegularFontFamily()
                KaigiFontFamily.RobotoMedium -> robotoMediumFontFamily()
                KaigiFontFamily.SystemDefault -> null
            }

            with(screenContext) {
                TestDefaultsProvider(
                    testDispatcher = testDispatcher,
                    fontFamily = fontFamily,
                ) {
                    SettingsScreenRoot(
                        onBackClick = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickSettingsItemRow() {
        composeUiTest
            .onNode(
                hasTestTag(SettingsAccessibilityUseFontFamilySettingsItemRowTestTag) and hasClickAction(),
                useUnmergedTree = true,
            )
            .performSemanticsAction(SemanticsActions.OnClick)
    }

    context(composeUiTest: ComposeUiTest)
    private fun clickEachUseFontItem(
        fontFamily: TestFontFamily,
    ) {
        val tag = SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix + fontFamily.displayName

        composeUiTest
            .onNode(hasTestTag(tag) and hasClickAction(), useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.OnClick)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickChangoRegularFontItem() {
        clickEachUseFontItem(TestFontFamily.ChangoRegular)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickRobotoRegularFontItem() {
        clickEachUseFontItem(TestFontFamily.RobotoRegular)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickRobotoMediumFontItem() {
        clickEachUseFontItem(TestFontFamily.RobotoMedium)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickSystemDefaultFontItem() {
        clickEachUseFontItem(TestFontFamily.SystemDefault)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollSettingsScreenLazyColumn(
        fontFamily: TestFontFamily,
    ) {
        composeUiTest
            .onNodeWithTag(SettingsScreenLazyColumnTestTag)
            .performScrollToNode(
                matcher = hasTestTag(
                    SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix.plus(fontFamily.displayName),
                ),
            )
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedAvailableFont(
        fontFamily: TestFontFamily,
    ) {
        composeUiTest
            .onNodeWithTag(SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix.plus(fontFamily.displayName))
            .assertIsDisplayed()
            .assertExists()
            .assertTextEquals(fontFamily.displayName)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkEachEnsureThatFontIsUsed(
        fontFamily: TestFontFamily,
    ) {
        composeUiTest
            .onNodeWithTag(
                testTag = SettingsItemRowCurrentValueTextTestTag,
                useUnmergedTree = true,
            )
            .assertTextEquals(fontFamily.displayName)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEnsureThatChangoFontIsUsed() {
        checkEachEnsureThatFontIsUsed(TestFontFamily.ChangoRegular)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEnsureThatRobotoRegularFontIsUsed() {
        checkEachEnsureThatFontIsUsed(TestFontFamily.RobotoRegular)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEnsureThatRobotoMediumFontIsUsed() {
        checkEachEnsureThatFontIsUsed(TestFontFamily.RobotoMedium)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEnsureThatSystemDefaultFontIsUsed() {
        checkEachEnsureThatFontIsUsed(TestFontFamily.SystemDefault)
    }
}

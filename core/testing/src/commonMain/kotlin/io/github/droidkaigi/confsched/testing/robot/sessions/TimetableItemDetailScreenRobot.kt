package io.github.droidkaigi.confsched.testing.robot.sessions

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenContext
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenRoot
import io.github.droidkaigi.confsched.sessions.components.DescriptionMoreButtonTestTag
import io.github.droidkaigi.confsched.sessions.components.SummaryCardTextTag
import io.github.droidkaigi.confsched.sessions.components.TargetAudienceSectionTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailBookmarkFabButtonTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailBookmarkMenuItemTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentArchiveSectionBottomTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentArchiveSectionSlideButtonTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentArchiveSectionTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentArchiveSectionVideoButtonTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentTargetAudienceSectionBottomTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailHeadlineTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailMessageRowTestTag
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultFontScaleRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.FontScaleRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class TimetableItemDetailScreenRobot(
    private val contextFactory: TimetableItemDetailScreenContext.Factory,
    private val testDispatcher: TestDispatcher,
    timetableServerRobot: DefaultTimetableServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
    fontScaleRobot: DefaultFontScaleRobot,
) : TimetableServerRobot by timetableServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot,
    FontScaleRobot by fontScaleRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupTimetableItemDetailScreenContent(
        timetableItemId: TimetableItemId = TimetableItemId(FakeSessionsApiClient.defaultSessionId),
    ) {
        val screenContext = with(contextFactory) {
            createTimetableDetailScreenContext(timetableItemId)
        }
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    TimetableItemDetailScreenRoot(
                        onBackClick = {},
                        onAddCalendarClick = {},
                        onShareClick = {},
                        onLinkClick = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun setupTimetableItemDetailScreenContentWithLongDescription() = setupTimetableItemDetailScreenContent(TimetableItemId(FakeSessionsApiClient.defaultSessionIdWithLongDescription))

    context(composeUiTest: ComposeUiTest)
    fun bookmark() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailBookmarkFabButtonTestTag)
            .performClick()
        waitUntilIdle()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailBookmarkMenuItemTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scroll() {
        composeUiTest
            .onRoot()
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 3F / 4,
                    endY = visibleSize.height / 4F,
                )
            }
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollLazyColumnByIndex(
        index: Int,
    ) {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToIndex(index)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollLazyColumnByTestTag(
        testTag: String,
    ) {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(hasTestTag(testTag))
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToReadMoreButton() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(hasTestTag(DescriptionMoreButtonTestTag))
    }

    // TODO This method is used when UI that needs to be confirmed under the target section is added.
    // TODO If it is determined that it will not be used after the specifications are finalized, please delete it.
    context(composeUiTest: ComposeUiTest)
    fun scrollToTargetAudienceSectionBottom() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(
                hasTestTag(
                    TimetableItemDetailContentTargetAudienceSectionBottomTestTag,
                ),
            )
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToArchiveSectionBottom() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(
                hasTestTag(
                    TimetableItemDetailContentArchiveSectionBottomTestTag,
                ),
            )
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToMessageRow() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(hasTestTag(TimetableItemDetailMessageRowTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the message section.
        composeUiTest.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 100)
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkSessionDetailTitle() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailHeadlineTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(FakeSessionsApiClient.defaultSession.title.ja)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkNotBookmarked() {
        composeUiTest
            .onNodeWithTag(
                "$TimetableItemDetailBookmarkIconTestTag:true",
                useUnmergedTree = true,
            )
            .assertDoesNotExist()

        composeUiTest
            .onNodeWithTag(
                "$TimetableItemDetailBookmarkIconTestTag:false",
                useUnmergedTree = true,
            )
            .assertExists()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkBookmarked() {
        composeUiTest
            .onNodeWithTag(
                "$TimetableItemDetailBookmarkIconTestTag:true",
                useUnmergedTree = true,
            )
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNodeWithTag(
                "$TimetableItemDetailBookmarkIconTestTag:false",
                useUnmergedTree = true,
            )
            .assertDoesNotExist()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTargetAudience() {
        composeUiTest
            .onNodeWithTag(TargetAudienceSectionTestTag)
            .onChildren()
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Target Audience")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkSummaryCardTexts(
        titles: List<String> = listOf(
            "Date/Time",
            "Location",
            "Supported Languages",
            "Category",
        ),
    ) {
        titles.forEach { title ->
            composeUiTest
                .onNodeWithTag(SummaryCardTextTag.plus(title))
                .assertExists()
                .assertIsDisplayed()
                .assertTextContains(
                    value = title,
                    substring = true,
                )
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayingMoreButton() {
        composeUiTest
            .onNodeWithTag(DescriptionMoreButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkBothOfSlidesButtonAndVideoButtonDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionTestTag)
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOnlySlidesButtonDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionTestTag)
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
            .assertDoesNotExist()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkOnlyVideoButtonDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionTestTag)
            .assertExists()
            .assertIsDisplayed()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
            .assertDoesNotExist()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkBothOfSlidesButtonAndVideoButtonNotDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionTestTag)
            .assertDoesNotExist()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
            .assertDoesNotExist()

        composeUiTest
            .onNodeWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
            .assertDoesNotExist()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkMessageDisplayed() {
        composeUiTest
            .onAllNodesWithTag(TimetableItemDetailMessageRowTestTag)
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("This session has been canceled.")
    }
}

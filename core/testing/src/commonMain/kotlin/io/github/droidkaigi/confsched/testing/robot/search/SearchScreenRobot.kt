package io.github.droidkaigi.confsched.testing.robot.search

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentTestTag
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultSuspenseFallbackContentTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCardSemanticsKey
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCardTitleTextTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableListTestTag
import io.github.droidkaigi.confsched.sessions.SearchScreenContext
import io.github.droidkaigi.confsched.sessions.SearchScreenRoot
import io.github.droidkaigi.confsched.sessions.components.DropdownFilterChipTestTagPrefix
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRowFilterCategoryChipTestTag
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRowFilterDayChipTestTag
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRowFilterLanguageChipTestTag
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRowFilterSessionTypeChipTestTag
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRowTestTag
import io.github.droidkaigi.confsched.sessions.components.SearchTopBarTextFieldTestTag
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot
import io.github.droidkaigi.confsched.testing.util.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.util.assertSemanticsProperty
import io.github.droidkaigi.confsched.testing.util.assertTextDoesNotContain
import io.github.droidkaigi.confsched.testing.util.onAllNodesWithTag
import kotlinx.coroutines.test.TestDispatcher

private const val DemoSearchWord = "Demo"

@Inject
class SearchScreenRobot(
    private val screenContext: SearchScreenContext,
    private val testDispatcher: TestDispatcher,
    timetableServerRobot: DefaultTimetableServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : TimetableServerRobot by timetableServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    enum class ConferenceDay(
        val day: Int,
        val dateText: String,
    ) {
        Day1(1, "9.11"),
        Day2(2, "9.12"),
    }

    enum class Category(
        val categoryName: String,
    ) {
        AppArchitecture("App Architecture en"),
        JetpackCompose("Jetpack Compose en"),
        Other("Other en"),
    }

    enum class SessionType(
        val label: String,
    ) {
        WelcomeTalk("Welcome Talk"),
        Normal("Session"),
    }

    enum class Language(
        val tagName: String,
    ) {
        Japanese("JA"),
        English("EN"),
    }

    context(composeUiTest: ComposeUiTest)
    fun setupSearchScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    SearchScreenRoot(
                        onTimetableItemClick = {},
                        onBackClick = {},
                    )
                }
            }
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkLoadingIndicatorDisplayed() {
        composeUiTest.onNodeWithTag(DefaultSuspenseFallbackContentTestTag).assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkErrorMessageDisplayed() {
        composeUiTest.onNodeWithTag(DefaultErrorFallbackContentTestTag).assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun inputDemoSearchWord() {
        inputSearchWord(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun inputSearchWord(text: String) {
        composeUiTest
            .onNodeWithTag(SearchTopBarTextFieldTestTag)
            .performTextInput(text)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToFilterLanguageChip() {
        composeUiTest
            .onNodeWithTag(SearchFilterRowTestTag)
            .performScrollToNode(hasTestTag(SearchFilterRowFilterLanguageChipTestTag))
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterDayChip() {
        composeUiTest
            .onNodeWithTag(SearchFilterRowFilterDayChipTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterCategoryChip() {
        composeUiTest
            .onNodeWithTag(SearchFilterRowFilterCategoryChipTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterSessionTypeChip() {
        composeUiTest
            .onNodeWithTag(SearchFilterRowFilterSessionTypeChipTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterLanguageChip() {
        composeUiTest
            .onNodeWithTag(SearchFilterRowFilterLanguageChipTestTag)
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickConferenceDay(
        clickDay: ConferenceDay,
    ) {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .filter(matcher = hasText(clickDay.dateText))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickCategory(
        category: Category,
    ) {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .filter(matcher = hasText(category.categoryName))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickSessionType(
        sessionType: SessionType,
    ) {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .filter(matcher = hasText(sessionType.label))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickLanguage(
        language: Language,
    ) {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .filter(matcher = hasText(language.tagName, ignoreCase = true, substring = true))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterDayChip() {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .assertCountAtLeast(ConferenceDay.entries.size)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterCategoryChip() {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .assertCountAtLeast(Category.entries.size)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterSessionTypeChip() {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .assertCountAtLeast(SessionType.entries.size)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterLanguageChip() {
        composeUiTest
            .onAllNodesWithTag(
                testTag = DropdownFilterChipTestTagPrefix,
                substring = true,
            )
            .assertCountAtLeast(Language.entries.size)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemByConferenceDay(
        checkDay: ConferenceDay,
    ) {
        val (contain, doesNotContain) = if (checkDay == ConferenceDay.Day1) {
            listOf(ConferenceDay.Day1, ConferenceDay.Day2)
        } else {
            listOf(ConferenceDay.Day2, ConferenceDay.Day1)
        }

        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertTextContains("Demo Welcome Talk ${contain.day}")
            .assertTextDoesNotContain("Demo Welcome Talk ${doesNotContain.day}")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemByCategory(
        category: Category,
    ) {
        val containText = if (category == Category.Other) {
            "Demo Welcome Talk"
        } else {
            category.categoryName
        }

        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertTextContains(
                value = containText,
                substring = true,
            )
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemBySessionType(
        sessionType: SessionType,
    ) {
        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertSemanticsProperty(TimetableItemCardSemanticsKey) { item ->
                item?.sessionType?.label?.enTitle == sessionType.label
            }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemByLanguage(language: Language) {
        val doesNotContains = Language.entries.filterNot { it == language }

        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertSemanticsProperty(TimetableItemCardSemanticsKey) { item ->
                item?.language?.toLang()?.tagName == language.tagName
            }

        doesNotContains.forEach { doesNotContain ->
            composeUiTest
                .onAllNodesWithTag(TimetableItemCardTestTag)
                .onFirst()
                .assertSemanticsProperty(TimetableItemCardSemanticsKey) { item ->
                    item?.language?.toLang()?.tagName != doesNotContain.tagName
                }
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDemoSearchWordDisplayed() {
        checkSearchWordDisplayed(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkSearchWordDisplayed(text: String) {
        composeUiTest
            .onNodeWithTag(SearchTopBarTextFieldTestTag)
            .assertTextEquals(text)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsHasDemoText() {
        checkTimetableListItemsHasText(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkTimetableListItemsHasText(searchWord: String) {
        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTitleTextTestTag)
            .assertAll(hasText(text = searchWord, ignoreCase = true))
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListNotDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableListTestTag)
            .assertIsNotDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListDisplayed() {
        composeUiTest
            .onNodeWithTag(TimetableListTestTag)
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsDisplayed() {
        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsNotDisplayed() {
        composeUiTest
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsNotDisplayed()
    }
}

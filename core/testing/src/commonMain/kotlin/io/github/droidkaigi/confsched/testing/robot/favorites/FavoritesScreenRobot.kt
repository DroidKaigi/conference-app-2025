package io.github.droidkaigi.confsched.testing.robot.favorites

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performCustomAccessibilityActionWithLabel
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentTestTag
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultSuspenseFallbackContentTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCardBookmarkButtonTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableListTestTag
import io.github.droidkaigi.confsched.favorites.FavoritesScreenContext
import io.github.droidkaigi.confsched.favorites.FavoritesScreenRoot
import io.github.droidkaigi.confsched.favorites.FavoritesScreenTestTag
import io.github.droidkaigi.confsched.favorites.section.FavoritesEmptyViewTestTag
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class FavoritesScreenRobot(
    private val screenContext: FavoritesScreenContext,
    private val testDispatcher: TestDispatcher,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val userDataStore: UserDataStore,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot,
    TimetableServerRobot by timetableServerRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupFavoritesScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    FavoritesScreenRoot(
                        onTimetableItemClick = {},
                    )
                }
            }
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkLoadingIndicatorDisplayed() {
        composeUiTest.onNodeWithTag(DefaultSuspenseFallbackContentTestTag).assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    suspend fun setupSingleFavoriteSession() {
        val firstSessionId = FakeSessionsApiClient.defaultSessionIds.first()
        userDataStore.toggleFavorite(TimetableItemId(firstSessionId))
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    suspend fun setupFavoriteSessions() {
        FakeSessionsApiClient.defaultSessionIds.forEach {
            userDataStore.toggleFavorite(TimetableItemId(it))
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFirstSessionBookmark() {
        composeUiTest.onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .performCustomAccessibilityActionWithLabel("Remove from Bookmarks")
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEmptyViewDisplayed() {
        composeUiTest.onNodeWithTag(FavoritesEmptyViewTestTag).assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsDisplayed() {
        composeUiTest
            .onAllNodes(hasTestTag(TimetableListTestTag))
            .onFirst()
            .assertIsDisplayed()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkErrorFallbackDisplayed() {
        composeUiTest.onNodeWithTag(DefaultErrorFallbackContentTestTag).assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListFirstItemNotDisplayed() {
        composeUiTest
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsNotDisplayed()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollFavorites() {
        composeUiTest
            .onNode(hasTestTag(FavoritesScreenTestTag))
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 4F / 5,
                    endY = visibleSize.height / 5F,
                )
            }
    }

    context(composeUiTest: ComposeUiTest)
    suspend fun clearFavorites() {
        userDataStore.clearFavorites()
        waitUntilIdle()
    }
}

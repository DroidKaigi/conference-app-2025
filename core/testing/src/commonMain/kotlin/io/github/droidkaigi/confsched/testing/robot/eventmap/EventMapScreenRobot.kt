package io.github.droidkaigi.confsched.testing.robot.eventmap

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentRetryTestTag
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapScreenContext
import io.github.droidkaigi.confsched.eventmap.EventMapScreenRoot
import io.github.droidkaigi.confsched.eventmap.component.EventMapDescriptionTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapItemTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapLazyColumnTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabImageTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabTestTagPrefix
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class EventMapScreenRobot(
    private val screenContext: EventMapScreenContext,
    private val testDispatcher: TestDispatcher,
    eventMapServerRobot: DefaultEventMapServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : EventMapServerRobot by eventMapServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    private enum class FloorLevel(
        val floorName: String,
    ) {
        Basement("B1F"),
        Ground("1F"),
    }

    private enum class RoomType {
        Jellyfish,
        Koala,
        Ladybug,
        Meerkat,
        Narwhal,
    }

    context(composeUiTest: ComposeUiTest)
    fun setupEventMapScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    EventMapScreenRoot(
                        onClickReadMore = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToJellyfishRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Jellyfish)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToKoalaRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Koala)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToLadybugRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Ladybug)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToMeerkatRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Meerkat)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToNarwhalRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Narwhal)
    }

    context(composeUiTest: ComposeUiTest)
    private fun scrollLazyColumnByRoomName(
        roomType: RoomType,
    ) {
        composeUiTest
            .onNodeWithTag(EventMapLazyColumnTestTag)
            .performScrollToNode(hasTestTag(EventMapItemTestTag.plus(roomType.name.toUpperCase(Locale.current))))
        waitFor5Seconds()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickEventMapTabOnGround() {
        clickEventMapTab(FloorLevel.Ground)
    }

    context(composeUiTest: ComposeUiTest)
    fun clickEventMapTabOnBasement() {
        clickEventMapTab(FloorLevel.Basement)
    }

    context(composeUiTest: ComposeUiTest)
    private fun clickEventMapTab(
        floorLevel: FloorLevel,
    ) {
        composeUiTest
            .onNodeWithTag(EventMapTabTestTagPrefix.plus(floorLevel.floorName))
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickRetryButton() {
        composeUiTest
            .onNodeWithTag(DefaultErrorFallbackContentRetryTestTag)
            .performClick()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapDescriptionDisplayed() {
        composeUiTest
            .onNodeWithTag(EventMapDescriptionTestTag)
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapItemJellyfish() {
        checkEventMapItemByRoomName(roomType = RoomType.Jellyfish)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapItemKoala() {
        checkEventMapItemByRoomName(roomType = RoomType.Koala)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapItemLadybug() {
        checkEventMapItemByRoomName(roomType = RoomType.Ladybug)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapItemMeerkat() {
        checkEventMapItemByRoomName(roomType = RoomType.Meerkat)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapItemNarwhal() {
        checkEventMapItemByRoomName(roomType = RoomType.Narwhal)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkEventMapItemByRoomName(
        roomType: RoomType,
    ) {
        composeUiTest
            .onAllNodesWithTag(EventMapItemTestTag.plus(roomType.name.toUpperCase(Locale.current)))
            .onFirst()
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapOnGround() {
        checkEventMap(FloorLevel.Ground)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkEventMapOnBasement() {
        checkEventMap(FloorLevel.Basement)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkEventMap(
        floorLevel: FloorLevel,
    ) {
        composeUiTest
            .onNodeWithTag(EventMapTabImageTestTag)
            .assertContentDescriptionEquals("Map of ${floorLevel.floorName}")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkErrorFallbackDisplayed() {
        composeUiTest
            .onNodeWithTag(DefaultErrorFallbackContentTestTag)
            .assertExists()
    }
}

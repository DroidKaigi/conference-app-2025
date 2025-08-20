package io.github.droidkaigi.confsched.testing.robot.staff

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentRetryTestTag
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultErrorFallbackContentTestTag
import io.github.droidkaigi.confsched.droidkaigiui.architecture.DefaultSuspenseFallbackContentTestTag
import io.github.droidkaigi.confsched.model.staff.Staff
import io.github.droidkaigi.confsched.model.staff.fakes
import io.github.droidkaigi.confsched.staff.StaffItemTestTagPrefix
import io.github.droidkaigi.confsched.staff.StaffScreenContext
import io.github.droidkaigi.confsched.staff.StaffScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.staff.StaffScreenRoot
import io.github.droidkaigi.confsched.staff.component.StaffItemImageTestTag
import io.github.droidkaigi.confsched.staff.component.StaffItemUserNameTextTestTag
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import io.github.droidkaigi.confsched.testing.util.assertCountAtLeast
import io.github.droidkaigi.confsched.testing.util.hasTestTag
import kotlinx.coroutines.test.TestDispatcher

@Inject
class StaffScreenRobot(
    private val screenContext: StaffScreenContext,
    private val testDispatcher: TestDispatcher,
    staffServerRobot: DefaultStaffServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : StaffServerRobot by staffServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupStaffScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    StaffScreenRoot(
                        onStaffItemClick = {},
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
    fun checkLoadingIndicatorNotDisplayed() {
        composeUiTest.onNodeWithTag(DefaultSuspenseFallbackContentTestTag).assertDoesNotExist()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToIndex10() {
        composeUiTest
            .onNodeWithTag(StaffScreenLazyColumnTestTag)
            .performScrollToIndex(10)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkShowFirstAndSecondStaffs() {
        checkRangeStaffItemsDisplayed(
            fromTo = 0..2,
        )
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkRangeStaffItemsDisplayed(
        fromTo: IntRange,
    ) {
        val staffList = Staff.fakes().subList(fromTo.first, fromTo.last)
        staffList.forEach { staff ->
            composeUiTest
                .onNodeWithTag(StaffItemTestTagPrefix.plus(staff.id))
                .assertExists()
                .assertIsDisplayed()

            composeUiTest
                .onNodeWithTag(
                    testTag = StaffItemImageTestTag.plus(staff.id),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertContentDescriptionEquals(staff.name)

            composeUiTest
                .onNodeWithTag(
                    testTag = StaffItemUserNameTextTestTag.plus(staff.id),
                    useUnmergedTree = true,
                )
                .assertExists()
                .assertIsDisplayed()
                .assertTextEquals(staff.name)
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkStaffItemsDisplayed() {
        // Check there are two staffs
        composeUiTest
            .onAllNodes(hasTestTag(StaffItemTestTagPrefix, substring = true))
            .assertCountAtLeast(2)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkErrorFallbackDisplayed() {
        composeUiTest.onNodeWithTag(DefaultErrorFallbackContentTestTag).assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickRetryButton() {
        composeUiTest.onNodeWithTag(DefaultErrorFallbackContentRetryTestTag).performClick()
    }
}

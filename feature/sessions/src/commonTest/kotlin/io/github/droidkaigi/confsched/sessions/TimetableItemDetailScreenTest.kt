package io.github.droidkaigi.confsched.sessions

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.sessions.components.SummaryCardTextTag
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createTimetableItemDetailScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class TimetableItemDetailScreenTest {
    val testAppGraph = createTimetableItemDetailScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.timetableItemDetailScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<TimetableItemDetailScreenRobot>("TimetableItemDetailScreen") {
        describe("when server is operational") {
            doIt {
                setupTimetableServer(ServerStatus.Operational)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                }
                itShould("show session detail title") {
                    captureScreenWithChecks(
                        checks = {
                            checkSessionDetailTitle()
                        },
                    )
                }

                // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
                // TODO Test two states: bookmarked and not bookmarked.

                describe("scroll") {
                    doIt {
                        scroll()
                    }
                    itShould("show scrolled session detail") {
                        captureScreenWithChecks(
                            checks = {
                                checkTargetAudience()
                            },
                        )
                    }
                }

                // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
                // TODO Test that both the transition methods to the video and slide assets are hidden.
            }
            describe("when the description is lengthy") {
                doIt {
                    setupTimetableItemDetailScreenContentWithLongDescription()
                    scrollToReadMoreButton()
                    waitUntilIdle()
                }
                itShould("display a more button") {
                    captureScreenWithChecks {
                        checkDisplayingMoreButton()
                    }
                }
            }
            describe("when font scale is small") {
                doIt {
                    setFontScale(0.5f)
                    setupTimetableItemDetailScreenContent()
                }
                itShould("show small font session detail") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts()
                        },
                    )
                }
            }
            describe("when font scale is large") {
                doIt {
                    setFontScale(1.5f)
                    setupTimetableItemDetailScreenContent()
                    scrollLazyColumnByTestTag(SummaryCardTextTag.plus("Category"))
                }
                itShould("show large font session detail") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts()
                        },
                    )
                }
            }
            describe("when font scale is huge") {
                doIt {
                    setFontScale(2.0f)
                    setupTimetableItemDetailScreenContent()
                    scrollLazyColumnByTestTag(SummaryCardTextTag.plus("Date/Time"))
                }
                itShould("show huge font session detail(top)") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts(
                                titles = listOf(
                                    "Date/Time",
                                ),
                            )
                        },
                    )
                }
            }
            describe("when font scale is huge") {
                doIt {
                    setFontScale(2.0f)
                    setupTimetableItemDetailScreenContent()
                    scrollLazyColumnByTestTag(SummaryCardTextTag.plus("Category"))
                }
                itShould("show huge font session detail(bottom)") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts(
                                titles = listOf(
                                    "Location",
                                    "Supported Languages",
                                    "Category",
                                ),
                            )
                        },
                    )
                }
            }
        }

        // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
        // TODO 1. Test that the transition means to both slide and video assets are displayed.
        // TODO 2. Test that the transition means to slide assets only are displayed.
        // TODO 3. Test that the transition means to video assets only are displayed.

        describe("when server is operational exists message") {
            doIt {
                setupTimetableServer(ServerStatus.OperationalMessageExists)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                    scrollToMessageRow()
                }
                itShould("display message") {
                    captureScreenWithChecks {
                        checkMessageDisplayed()
                    }
                }
            }
        }
    }
}

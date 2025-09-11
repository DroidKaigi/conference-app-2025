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
                            checkNotBookmarked()
                        },
                    )
                }

                describe("bookmark") {
                    doIt {
                        bookmark()
                    }
                    itShould("show bookmarked icon") {
                        captureScreenWithChecks(
                            checks = {
                                checkBookmarked()
                            },
                        )
                    }
                }

                describe("scroll") {
                    doIt {
                        scroll()
                    }
                    itShould("show scrolled session detail") {
                        captureScreenWithChecks(
                            checks = {
                                checkTargetAudience()
                                checkBothOfSlidesButtonAndVideoButtonNotDisplayed()
                            },
                        )
                    }
                }
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

        describe("when server is operational both asset available") {
            doIt {
                setupTimetableServer(ServerStatus.OperationalBothAssetAvailable)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                    scrollToArchiveSectionBottom()
                }
                itShould("display slide and video buttons") {
                    captureScreenWithChecks {
                        checkBothOfSlidesButtonAndVideoButtonDisplayed()
                    }
                }
            }
        }

        describe("when server is operational only slide asset available") {
            doIt {
                setupTimetableServer(ServerStatus.OperationalOnlySlideAssetAvailable)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                    scrollToArchiveSectionBottom()
                }
                itShould("display only slide button") {
                    captureScreenWithChecks {
                        checkOnlySlidesButtonDisplayed()
                    }
                }
            }
        }

        describe("when server is operational only video asset available") {
            doIt {
                setupTimetableServer(ServerStatus.OperationalOnlyVideoAssetAvailable)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                    scrollToArchiveSectionBottom()
                }
                itShould("display only video button") {
                    captureScreenWithChecks {
                        checkOnlyVideoButtonDisplayed()
                    }
                }
            }
        }

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

package io.github.droidkaigi.confsched.sessions

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createSearchScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.search.SearchScreenRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class SearchScreenTest {

    val testAppGraph = createSearchScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.searchScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<SearchScreenRobot>("SearchScreen") {
        describe("when server is operational") {
            doIt {
                setupTimetableServer(ServerStatus.Operational)
                setupSearchScreenContent()
            }
            itShould("show loading indicator") {
                captureScreenWithChecks {
                    checkLoadingIndicatorDisplayed()
                }
            }
            describe("after loading") {
                doIt {
                    waitFor5Seconds()
                }
                itShould("no timetable items are displayed") {
                    captureScreenWithChecks {
                        checkTimetableListNotDisplayed()
                        checkTimetableListItemsNotDisplayed()
                    }
                }
                describe("input search word to TextField") {
                    doIt {
                        inputDemoSearchWord()
                    }
                    itShould("show search word and filtered items") {
                        captureScreenWithChecks {
                            checkDemoSearchWordDisplayed()
                            checkTimetableListItemsHasDemoText()
                            checkTimetableListDisplayed()
                            checkTimetableListItemsDisplayed()
                        }
                    }
                }
                describe("when filter day chip click") {
                    doIt {
                        clickFilterDayChip()
                    }
                    itShould("show drop down menu") {
                        captureScreenWithChecks {
                            checkDisplayedFilterDayChip()
                        }
                    }
                    SearchScreenRobot.ConferenceDay.entries.forEach { conference ->
                        describe("when click ${conference.day}") {
                            doIt {
                                clickConferenceDay(
                                    clickDay = conference,
                                )
                            }
                            itShould("selected ${conference.day}") {
                                captureScreenWithChecks {
                                    checkTimetableListItemByConferenceDay(
                                        checkDay = conference,
                                    )
                                    checkTimetableListDisplayed()
                                    checkTimetableListItemsDisplayed()
                                }
                            }
                        }
                    }
                }
                describe("when filter category chip click") {
                    doIt {
                        clickFilterCategoryChip()
                    }
                    itShould("show drop down menu") {
                        captureScreenWithChecks {
                            checkDisplayedFilterCategoryChip()
                        }
                    }
                    SearchScreenRobot.Category.entries.forEach { category ->
                        describe("when click ${category.categoryName}") {
                            doIt {
                                clickCategory(
                                    category = category,
                                )
                            }
                            itShould("selected ${category.categoryName}") {
                                captureScreenWithChecks {
                                    checkTimetableListItemByCategory(category)
                                    checkTimetableListDisplayed()
                                    checkTimetableListItemsDisplayed()
                                }
                            }
                        }
                    }
                }
                describe("when filter session type chip click") {
                    doIt {
                        clickFilterSessionTypeChip()
                    }
                    itShould("show drop down menu") {
                        captureScreenWithChecks {
                            checkDisplayedFilterSessionTypeChip()
                        }
                    }
                    SearchScreenRobot.SessionType.entries.forEach { sessionType ->
                        describe("when click ${sessionType.name}") {
                            doIt {
                                clickSessionType(
                                    sessionType = sessionType,
                                )
                            }
                            itShould("selected ${sessionType.name}") {
                                captureScreenWithChecks {
                                    checkTimetableListItemBySessionType(sessionType)
                                    checkTimetableListDisplayed()
                                    checkTimetableListItemsDisplayed()
                                }
                            }
                        }
                    }
                }
                describe("when filter language chip click") {
                    doIt {
                        scrollToFilterLanguageChip()
                        clickFilterLanguageChip()
                    }
                    itShould("show drop down menu") {
                        captureScreenWithChecks {
                            checkDisplayedFilterLanguageChip()
                        }
                    }
                    SearchScreenRobot.Language.entries.forEach { language ->
                        describe("when click ${language.name}") {
                            doIt {
                                clickLanguage(
                                    language = language,
                                )
                            }
                            itShould("selected ${language.name}") {
                                captureScreenWithChecks {
                                    checkTimetableListItemByLanguage(language)
                                    checkTimetableListDisplayed()
                                    checkTimetableListItemsDisplayed()
                                }
                            }
                        }
                    }
                }
            }
        }
        describe("when server is error") {
            doIt {
                setupTimetableServer(ServerStatus.Error)
                setupSearchScreenContent()
            }
            itShould("show loading indicator") {
                captureScreenWithChecks {
                    checkLoadingIndicatorDisplayed()
                }
            }
            describe("after loading") {
                doIt {
                    waitFor5Seconds()
                }
                itShould("show error message") {
                    captureScreenWithChecks {
                        checkErrorMessageDisplayed()
                    }
                }
            }
        }
    }
}

package io.github.droidkaigi.confsched.staff

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createStaffScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.staff.StaffScreenRobot
import io.github.droidkaigi.confsched.testing.robot.staff.StaffServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class StaffScreenTest {
    val testAppGraph = createStaffScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.staffScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<StaffScreenRobot>("StaffScreen") {
        describe("when server is operational") {
            doIt {
                setupStaffServer(ServerStatus.Operational)
            }
            describe("when launch") {
                doIt {
                    setupStaffScreenContent()
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
                    itShould("show first and second staffs") {
                        captureScreenWithChecks {
                            checkShowFirstAndSecondStaffs()
                        }
                    }

                    describe("when scroll to index 10") {
                        doIt {
                            scrollToIndex10()
                        }
                        itShould("show staffs") {
                            captureScreenWithChecks {
                                checkStaffItemsDisplayed()
                            }
                        }
                    }
                }
            }
        }
        describe("when server is error") {
            doIt {
                setupStaffServer(ServerStatus.Error)
            }
            describe("when launch") {
                doIt {
                    setupStaffScreenContent()
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
                            checkErrorFallbackDisplayed()
                        }
                    }
                    describe("click retry after server gets operational") {
                        doIt {
                            setupStaffServer(ServerStatus.Operational)
                            clickRetryButton()
                        }
                        describe("after waiting for 5 seconds") {
                            doIt {
                                waitFor5Seconds()
                            }
                            itShould("show staffs") {
                                captureScreenWithChecks {
                                    checkLoadingIndicatorNotDisplayed()
                                    checkStaffItemsDisplayed()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

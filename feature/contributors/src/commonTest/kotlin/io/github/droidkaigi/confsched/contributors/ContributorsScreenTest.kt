package io.github.droidkaigi.confsched.contributors

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createContributorsScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.contributors.ContributorsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.contributors.ContributorsServerRobot

@RunWith(UiTestRunner::class)
class ContributorsScreenTest {

    val testAppGraph = createContributorsScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.contributorsScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<ContributorsScreenRobot>(name = "ContributorsScreen") {
        describe("when server is operational") {
            doIt {
                setupContributorServer(ContributorsServerRobot.ServerStatus.Operational)
                setupContributorsScreenContent()
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
                itShould("show first and second contributors") {
                    captureScreenWithChecks {
                        checkShowFirstAndSecondContributors()
                    }
                }
                itShould("show contributors total count") {
                    captureScreenWithChecks {
                        checkContributorTotalCountDisplayed()
                    }
                }

                describe("when scroll to index 10") {
                    doIt {
                        scrollToIndex10()
                    }
                    itShould("show contributors") {
                        captureScreenWithChecks {
                            checkContributorItemsDisplayed()
                        }
                    }
                }
            }
        }
        describe("when server is down") {
            doIt {
                setupContributorServer(ContributorsServerRobot.ServerStatus.Error)
                setupContributorsScreenContent()
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
                itShould("does not show contributors") {
                    captureScreenWithChecks(
                        checks = {
                            checkDoesNotFirstContributorItemDisplayed()
                        },
                    )
                }
            }
        }
    }
}

package io.github.droidkaigi.confsched.profile

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createProfileScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.profile.ProfileDataStoreRobot.ProfileInputStatus
import io.github.droidkaigi.confsched.testing.robot.profile.ProfileScreenRobot

@RunWith(UiTestRunner::class)
class ProfileScreenTest {
    val testAppGraph = createProfileScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.profileScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<ProfileScreenRobot>("ProfileScreen") {
        describe("when profile card is does not exists") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.AllNotEntered)
                setupProfileScreenContent()
            }
            itShould("show profile edit screen") {
                captureScreenWithChecks {
                    checkEditScreenDisplayed()
                }
            }
        }
        describe("when profile card is exists") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.AllEntered)
                setupProfileScreenContent()
            }
            itShould("show profile card screen") {
                captureScreenWithChecks {
                    checkCardScreenDisplayed()
                }
            }
        }
    }
}

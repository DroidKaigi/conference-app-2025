package io.github.droidkaigi.confsched.profile

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import io.github.droidkaigi.confsched.model.profile.fake
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
            describe("fill out all text forms") {
                val profile = Profile.fake()
                doIt {
                    inputName(profile.nickName)
                    inputOccupation(profile.occupation)
                    inputLink(profile.link)
                }
                itShould("show the entered information correctly") {
                    captureScreenWithChecks {
                        checkName(profile.nickName)
                        checkOccupation(profile.occupation)
                        checkLink(profile.link)
                    }
                }
            }
            describe("when changing card themes") {
                doIt {
                    clickCardTheme(ProfileCardTheme.LightDiamond)
                }
                itShould("is changed to the selected theme") {
                    captureScreenWithChecks {
                        checkThemeSelected(ProfileCardTheme.LightDiamond)
                    }
                }
            }
            describe("click create card button when profile is empty") {
                doIt {
                    clickCreateButton()
                }
                itShould("show error text") {
                    captureScreenWithChecks {
                        checkNameError()
                        checkOccupationError()
                        checkLinkEmptyError()
                        checkImageEmptyError()
                    }
                }
            }
            describe("check invalid link") {
                doIt {
                    inputLink("あいうえお")
                    clickCreateButton()
                }
                itShould("show error text") {
                    captureScreenWithChecks {
                        checkLinkInvalidError()
                    }
                }
            }
        }
        describe("when profile is saved without image") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.NoInputOtherThanImage)
                setupProfileScreenContent()
            }
            itShould("show saved content") {
                val profile = Profile.fake()
                captureScreenWithChecks {
                    checkEditScreenDisplayed()
                    checkName(profile.nickName)
                    checkOccupation(profile.occupation)
                    checkLink(profile.link)
                    checkThemeSelected(profile.theme)
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
                    checkCardFrontDisplayed()
                }
            }
            describe("flip the card") {
                doIt {
                    flipProfileCard()
                }
                itShould("show the back of the card") {
                    captureScreenWithChecks {
                        checkCardBackDisplayed()
                    }
                }
            }
            describe("when click edit button") {
                doIt {
                    clickEditButton()
                }
                itShould("back to the profile edit screen") {
                    val profile = Profile.fake()
                    captureScreenWithChecks {
                        checkEditScreenDisplayed()
                        checkName(profile.nickName)
                        checkOccupation(profile.occupation)
                        checkLink(profile.link)
                    }
                }
            }
        }
    }
}

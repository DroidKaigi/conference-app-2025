package io.github.droidkaigi.confsched.profile

import androidx.compose.ui.test.runComposeUiTest
import io.github.confsched.profile.ProfileCardEditButtonTestTag
import io.github.confsched.profile.ProfileCardEditCreateCardButtonTestTag
import io.github.confsched.profile.ProfileCardEditLinkFormLabelTestTag
import io.github.confsched.profile.ProfileCardEditNameFormTestTag
import io.github.confsched.profile.ProfileCardEditThemeTestTag
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
        describe("when the profile card does not exist") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.AllNotEntered)
                setupProfileScreenContent()
            }
            itShould("show profile edit screen") {
                captureScreenWithChecks {
                    checkEditScreenDisplayed()
                }
            }
            describe("when filling out all text fields") {
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
                    scrollToNodeTagInEditScreen(ProfileCardEditThemeTestTag.plus(ProfileCardTheme.LightDiamond))
                    clickCardTheme(ProfileCardTheme.LightDiamond)
                }
                itShould("change to the selected theme") {
                    captureScreenWithChecks {
                        checkThemeSelected(ProfileCardTheme.LightDiamond)
                    }
                }
            }
            describe("when the profile is empty and the create card button is clicked") {
                doIt {
                    scrollToNodeTagInEditScreen(ProfileCardEditCreateCardButtonTestTag)
                    clickCreateButton()
                    scrollToNodeTagInEditScreen(ProfileCardEditNameFormTestTag)
                }
                itShould("show error messages") {
                    captureScreenWithChecks {
                        checkNameError()
                        checkOccupationError()
                        checkLinkEmptyError()
                        checkImageEmptyError()
                    }
                }
            }
            describe("when entering an invalid link") {
                doIt {
                    inputLink("あいうえお")
                    scrollToNodeTagInEditScreen(ProfileCardEditCreateCardButtonTestTag)
                    clickCreateButton()
                    scrollToNodeTagInEditScreen(ProfileCardEditLinkFormLabelTestTag)
                }
                itShould("show an error message") {
                    captureScreenWithChecks {
                        checkLinkInvalidError()
                    }
                }
            }
        }
        describe("when the profile is saved without an image") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.NoInputOtherThanImage)
                setupProfileScreenContent()
            }
            itShould("show the saved content") {
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
        describe("when the profile card exists") {
            doIt {
                setupProfileDataStore(ProfileInputStatus.AllEntered)
                setupProfileScreenContent()
            }
            itShould("show the profile card screen") {
                captureScreenWithChecks {
                    checkCardScreenDisplayed()
                    checkCardFrontDisplayed()
                }
            }
            describe("when flipping the card") {
                doIt {
                    flipProfileCard()
                }
                itShould("show the back of the card") {
                    captureScreenWithChecks {
                        checkCardBackDisplayed()
                    }
                }
            }
            describe("when clicking the edit button") {
                doIt {
                    scrollToNodeTagInCardScreen(ProfileCardEditButtonTestTag)
                    clickEditButton()
                }
                itShould("return to the profile edit screen") {
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

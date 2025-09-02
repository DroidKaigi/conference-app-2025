package io.github.droidkaigi.confsched.settings

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createSettingsScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.settings.SettingsDataStoreRobot
import io.github.droidkaigi.confsched.testing.robot.settings.SettingsScreenRobot

@RunWith(UiTestRunner::class)
class SettingsScreenTest {
    val testAppGraph = createSettingsScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.settingsScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<SettingsScreenRobot>("SettingsScreenRobot") {
        SettingsDataStoreRobot.SettingsStatus.entries.forEach {
            describe("when launch use font ${it.displayName}") {
                doIt {
                    setupSettings(it)
                    setupSettingsScreenContent()
                }
                itShould("show settings contents") {
                    captureScreenWithChecks {
                        when (it) {
                            SettingsDataStoreRobot.SettingsStatus.UseChangoRegularFontFamily -> checkEnsureThatChangoFontIsUsed()
                            SettingsDataStoreRobot.SettingsStatus.UseRobotoRegularFontFamily -> checkEnsureThatRobotoRegularFontIsUsed()
                            SettingsDataStoreRobot.SettingsStatus.UseRobotoMediumFontFamily -> checkEnsureThatRobotoMediumFontIsUsed()
                            SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont -> checkEnsureThatSystemDefaultFontIsUsed()
                        }
                    }
                }
                describe("when click use font item") {
                    doIt {
                        clickSettingsItemRow()
                    }
                    SettingsScreenRobot.TestFontFamily.entries.forEach { fontFamily ->
                        itShould("show available $fontFamily font") {
                            scrollSettingsScreenLazyColumn(fontFamily)
                            captureScreenWithChecks {
                                checkDisplayedAvailableFont(fontFamily)
                            }
                        }
                    }
                    SettingsDataStoreRobot.SettingsStatus.entries.forEach { clickedStatus ->
                        describe("when click ${clickedStatus.displayName} font") {
                            doIt {
                                when (clickedStatus) {
                                    SettingsDataStoreRobot.SettingsStatus.UseChangoRegularFontFamily -> clickChangoRegularFontItem()
                                    SettingsDataStoreRobot.SettingsStatus.UseRobotoRegularFontFamily -> clickRobotoRegularFontItem()
                                    SettingsDataStoreRobot.SettingsStatus.UseRobotoMediumFontFamily -> clickRobotoMediumFontItem()
                                    SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont -> clickSystemDefaultFontItem()
                                }
                            }
                            itShould("selected ${clickedStatus.displayName} font") {
                                captureScreenWithChecks {
                                    when (clickedStatus) {
                                        SettingsDataStoreRobot.SettingsStatus.UseChangoRegularFontFamily -> checkEnsureThatChangoFontIsUsed()
                                        SettingsDataStoreRobot.SettingsStatus.UseRobotoRegularFontFamily -> checkEnsureThatRobotoRegularFontIsUsed()
                                        SettingsDataStoreRobot.SettingsStatus.UseRobotoMediumFontFamily -> checkEnsureThatRobotoMediumFontIsUsed()
                                        SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont -> checkEnsureThatSystemDefaultFontIsUsed()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

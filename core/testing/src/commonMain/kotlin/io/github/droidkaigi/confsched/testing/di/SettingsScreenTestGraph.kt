package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.settings.SettingsScreenContext
import io.github.droidkaigi.confsched.testing.robot.settings.SettingsScreenRobot

interface SettingsScreenTestGraph : SettingsScreenContext.Factory {
    val settingsScreenRobotProvider: Provider<SettingsScreenRobot>

    @Provides
    fun provideSettingsScreenContext(): SettingsScreenContext {
        return createSettingsScreenContext()
    }
}

fun createSettingsScreenTestGraph(): SettingsScreenTestGraph = createTestAppGraph()

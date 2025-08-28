package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.confsched.profile.ProfileScreenContext
import io.github.droidkaigi.confsched.testing.robot.profile.ProfileScreenRobot

interface ProfileScreenTestGraph : ProfileScreenContext.Factory {
    val profileScreenRobotProvider: Provider<ProfileScreenRobot>

    @Provides
    fun provideProfileScreenContext(): ProfileScreenContext {
        return createProfileScreenContext()
    }
}

fun createProfileScreenTestGraph(): ProfileScreenTestGraph = createTestAppGraph()

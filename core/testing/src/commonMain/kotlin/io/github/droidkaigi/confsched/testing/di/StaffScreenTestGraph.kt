package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.staff.StaffScreenContext
import io.github.droidkaigi.confsched.testing.robot.staff.StaffScreenRobot

interface StaffScreenTestGraph : StaffScreenContext.Factory {
    val staffScreenRobotProvider: Provider<StaffScreenRobot>

    @Provides
    fun provideStaffScreenContext(): StaffScreenContext {
        return createStaffScreenContext()
    }
}

fun createStaffScreenTestGraph(): StaffScreenTestGraph = createTestAppGraph()

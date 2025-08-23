package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenContext
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableItemDetailScreenRobot

interface TimetableItemDetailScreenTestGraph : TimetableItemDetailScreenContext.Factory {
    val timetableItemDetailScreenRobotProvider: Provider<TimetableItemDetailScreenRobot>
}

fun createTimetableItemDetailScreenTestGraph(): TimetableItemDetailScreenTestGraph = createTestAppGraph()

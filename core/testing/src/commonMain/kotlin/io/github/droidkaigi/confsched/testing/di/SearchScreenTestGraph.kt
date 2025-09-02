package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.sessions.SearchScreenContext
import io.github.droidkaigi.confsched.testing.robot.search.SearchScreenRobot

interface SearchScreenTestGraph : SearchScreenContext.Factory {
    val searchScreenRobotProvider: Provider<SearchScreenRobot>

    @Provides
    fun provideSearchScreenContext(): SearchScreenContext {
        // The `rememberRetained` key is not required for testing, so we've set it to `0L`.
        return createSearchScreenContext(0L)
    }
}

fun createSearchScreenTestGraph(): SearchScreenTestGraph = createTestAppGraph()

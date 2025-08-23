package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.favorites.FavoritesScreenContext
import io.github.droidkaigi.confsched.testing.robot.favorites.FavoritesScreenRobot

interface FavoritesScreenTestGraph : FavoritesScreenContext.Factory {
    val favoritesScreenRobotProvider: Provider<FavoritesScreenRobot>

    @Provides
    fun provideFavoritesScreenContext(): FavoritesScreenContext {
        return createFavoritesScreenContext()
    }
}

fun createFavoritesScreenTestGraph(): FavoritesScreenTestGraph = createTestAppGraph()

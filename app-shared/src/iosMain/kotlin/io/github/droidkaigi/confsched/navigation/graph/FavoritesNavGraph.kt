package io.github.droidkaigi.confsched.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.favorites.FavoritesScreenRoot
import io.github.droidkaigi.confsched.favorites.rememberFavoritesScreenContextRetained
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.navigation.route.Favorites
import io.github.droidkaigi.confsched.navigation.route.FavoritesTabRoute

context(appGraph: AppGraph)
fun NavGraphBuilder.favoritesTabNavGraph(
    onBackClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onNavigateToListClick: () -> Unit,
) {
    navigation<FavoritesTabRoute>(
        startDestination = Favorites,
    ) {
        favoritesNavGraph(onTimetableItemClick = onTimetableItemClick)
        timetableItemDetailNavGraph(
            onBackClick = onBackClick,
            onLinkClick = onLinkClick,
            onShareClick = onShareClick,
            onAddCalendarClick = onAddCalendarClick,
            onNavigateToListClick = onNavigateToListClick,
        )
    }
}

context(appGraph: AppGraph)
private fun NavGraphBuilder.favoritesNavGraph(
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    composable<Favorites> {
        with(rememberFavoritesScreenContextRetained()) {
            FavoritesScreenRoot(
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}

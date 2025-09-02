package io.github.droidkaigi.confsched.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.navigation.route.SearchRoute
import io.github.droidkaigi.confsched.navigation.route.TimetableItemDetailRoute
import io.github.droidkaigi.confsched.navigation.route.TimetableRoute
import io.github.droidkaigi.confsched.navigation.route.TimetableTabRoute
import io.github.droidkaigi.confsched.sessions.SearchScreenRoot
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenRoot
import io.github.droidkaigi.confsched.sessions.TimetableScreenRoot
import io.github.droidkaigi.confsched.sessions.rememberSearchScreenContextRetained
import io.github.droidkaigi.confsched.sessions.rememberTimetableItemDetailScreenContextRetained
import io.github.droidkaigi.confsched.sessions.rememberTimetableScreenContextRetained

context(appGraph: AppGraph)
fun NavGraphBuilder.timetableTabNavGraph(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onBackClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
) {
    navigation<TimetableTabRoute>(
        startDestination = TimetableRoute,
    ) {
        timetableNavGraph(
            onSearchClick = onSearchClick,
            onTimetableItemClick = onTimetableItemClick,
        )
        timetableItemDetailNavGraph(
            onBackClick = onBackClick,
            onLinkClick = onLinkClick,
            onShareClick = onShareClick,
            onAddCalendarClick = onAddCalendarClick,
        )
        searchNavGraph(
            onBackClick = onBackClick,
            onTimetableItemClick = onTimetableItemClick,
        )
    }
}

context(appGraph: AppGraph)
private fun NavGraphBuilder.timetableNavGraph(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    composable<TimetableRoute> {
        with(rememberTimetableScreenContextRetained()) {
            TimetableScreenRoot(
                onSearchClick = onSearchClick,
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun NavGraphBuilder.timetableItemDetailNavGraph(
    onBackClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
) {
    composable<TimetableItemDetailRoute> {
        val timetableItemId =
            TimetableItemId(it.toRoute<TimetableItemDetailRoute>().id)
        with(rememberTimetableItemDetailScreenContextRetained(timetableItemId)) {
            TimetableItemDetailScreenRoot(
                onBackClick = onBackClick,
                onLinkClick = onLinkClick,
                onShareClick = onShareClick,
                onAddCalendarClick = onAddCalendarClick,
            )
        }
    }
}

context(appGraph: AppGraph)
private fun NavGraphBuilder.searchNavGraph(
    onBackClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    composable<SearchRoute> {
        // Only on the JVM is it necessary to specify the `rememberRetained` key.
        // This is not required on iOS.
        with(rememberSearchScreenContextRetained(0L)) {
            SearchScreenRoot(
                onBackClick = onBackClick,
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}

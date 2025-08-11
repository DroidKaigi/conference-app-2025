package io.github.droidkaigi.confsched.favorites

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

context(screenContext: FavoritesScreenContext)
@Composable
fun FavoritesScreenRoot(
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    SoilDataBoundary(
        state1 = rememberQuery(screenContext.timetableQueryKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
        suspenseFallback = { DefaultSuspenseFallback(title = stringResource(FavoritesRes.string.favorite)) },
        errorFallback = { DefaultErrorFallback(it, title = stringResource(FavoritesRes.string.favorite)) },
    ) { timetable, favoriteTimetableItemIds ->
        val eventFlow = rememberEventFlow<FavoritesScreenEvent>()

        val uiState = favoritesScreenPresenter(
            eventFlow = eventFlow,
            timetable = timetable.copy(bookmarks = favoriteTimetableItemIds),
        )

        FavoritesScreen(
            uiState = uiState,
            onTimetableItemClick = onTimetableItemClick,
            onBookmarkClick = { eventFlow.tryEmit(FavoritesScreenEvent.Bookmark(it)) },
            onAllFilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterAllDays) },
            onDay1FilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterDay1) },
            onDay2FilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterDay2) },
        )
    }
}

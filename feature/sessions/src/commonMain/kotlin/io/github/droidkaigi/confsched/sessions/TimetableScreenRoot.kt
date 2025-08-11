package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultErrorFallback
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallback
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

context(screenContext: TimetableScreenContext)
@Composable
fun TimetableScreenRoot(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    SoilDataBoundary(
        state1 = rememberQuery(screenContext.timetableQueryKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
        suspenseFallback = { DefaultSuspenseFallback(title = stringResource(SessionsRes.string.timetable)) },
        errorFallback = { DefaultErrorFallback(it, title = stringResource(SessionsRes.string.timetable)) }
    ) { timetable, favoriteTimetableItemIds ->
        val eventFlow = rememberEventFlow<TimetableScreenEvent>()

        val uiState = timetableScreenPresenter(
            eventFlow = eventFlow,
            timetable = timetable.copy(bookmarks = favoriteTimetableItemIds),
        )

        TimetableScreen(
            uiState = uiState,
            onSearchClick = onSearchClick,
            onTimetableItemClick = onTimetableItemClick,
            onBookmarkClick = { sessionId ->
                eventFlow.tryEmit(TimetableScreenEvent.Bookmark(sessionId))
            },
            onTimetableUiChangeClick = { eventFlow.tryEmit(TimetableScreenEvent.UiTypeChange) },
        )
    }
}

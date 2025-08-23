package io.github.droidkaigi.confsched.sessions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: TimetableScreenContext)
fun TimetableScreenRoot(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    SoilDataBoundary(
        state1 = rememberQuery(screenContext.timetableQueryKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(
            title = stringResource(SessionsRes.string.timetable),
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            contentBackground = {
                TimetableBackground()
            },
        ),
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
            onDaySelected = { day ->
                eventFlow.tryEmit(TimetableScreenEvent.SelectTab(day))
            },
        )
    }
}

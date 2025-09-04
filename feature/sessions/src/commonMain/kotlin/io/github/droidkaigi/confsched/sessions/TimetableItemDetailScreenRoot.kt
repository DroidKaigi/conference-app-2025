package io.github.droidkaigi.confsched.sessions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: TimetableItemDetailScreenContext)
fun TimetableItemDetailScreenRoot(
    onBackClick: () -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onLinkClick: (url: String) -> Unit,
) {
    SoilDataBoundary(
        state1 = rememberQuery(screenContext.timetableItemQueryKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(
            title = "", // empty title, showing only back navigation
            onBackClick = onBackClick,
        ),
    ) { timetableItem, favoriteTimetableItemIds ->
        val eventFlow = rememberEventFlow<TimetableItemDetailScreenEvent>()

        val uiState = timetableItemDetailScreenPresenter(
            eventFlow = eventFlow,
            timetableItem = timetableItem,
            favoriteTimetableItemIds = favoriteTimetableItemIds,
        )

        TimetableItemDetailScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onBookmarkToggle = { eventFlow.tryEmit(TimetableItemDetailScreenEvent.Bookmark) },
            onAddCalendarClick = onAddCalendarClick,
            onShareClick = onShareClick,
            onLanguageSelect = { lang -> eventFlow.tryEmit(TimetableItemDetailScreenEvent.LanguageSelect(lang)) },
            onLinkClick = onLinkClick,
        )
    }
}

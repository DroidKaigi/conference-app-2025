package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableList
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.SearchScreenUiState.TimeSlot
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRow
import io.github.droidkaigi.confsched.sessions.components.SearchNotFoundContent
import io.github.droidkaigi.confsched.sessions.components.SearchTopBar
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun SearchScreen(
    uiState: SearchScreenUiState,
    onBackClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onEvent: (SearchScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                searchQuery = uiState.searchQuery,
                onQueryChange = { onEvent(SearchScreenEvent.Search(it)) },
                onBackClick = onBackClick,
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
        ) {
            SearchFilterRow(
                filters = uiState.availableFilters,
                onFilterToggle = { filter ->
                    onEvent(SearchScreenEvent.ToggleFilter(filter))
                },
            )

            when {
                !uiState.hasSearchCriteria -> {}
                uiState.groupedSessions.isEmpty() -> {
                    SearchNotFoundContent(
                        searchQuery = uiState.searchQuery,
                    )
                }
                else -> {
                    TimetableList(
                        timetableItemMap = uiState.groupedSessions,
                        onTimetableItemClick = onTimetableItemClick,
                        onBookmarkClick = { sessionId ->
                            onEvent(SearchScreenEvent.Bookmark(sessionId.value))
                        },
                        isBookmarked = { timetableItemId ->
                            uiState.bookmarks.contains(timetableItemId)
                        },
                        contentPadding = PaddingValues(16.dp),
                        highlightWord = uiState.searchQuery,
                    )
                }
            }
        }
    }
}

private class SearchScreenPreviewParameterProvider : PreviewParameterProvider<PersistentMap<TimeSlot, List<TimetableItem>>> {
    override val values: Sequence<PersistentMap<TimeSlot, List<TimetableItem>>> = sequenceOf(
        persistentMapOf(
            TimeSlot(
                startTime = LocalTime.parse("11:20"),
                endTime = LocalTime.parse("12:00"),
            ) to listOf(
                TimetableItem.Session.fake(),
            ),
        ),
        persistentMapOf(),
    )
}

@Preview
@Composable
private fun SearchScreenPreview(
    @PreviewParameter(SearchScreenPreviewParameterProvider::class) groupedSessions: PersistentMap<TimeSlot, List<TimetableItem>>,
) {
    KaigiPreviewContainer {
        SearchScreen(
            uiState = SearchScreenUiState(
                searchQuery = "DroidKaigi",
                groupedSessions = groupedSessions,
                availableFilters = SearchScreenUiState.Filters(
                    availableDays = listOf(
                        DroidKaigi2025Day.ConferenceDay1,
                        DroidKaigi2025Day.ConferenceDay2,
                    ),
                ),
                hasSearchCriteria = true,
            ),
            onBackClick = {},
            onTimetableItemClick = {},
            onEvent = {},
        )
    }
}

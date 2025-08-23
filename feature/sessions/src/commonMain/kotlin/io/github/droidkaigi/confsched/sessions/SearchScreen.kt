package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableList
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.sessions.components.SearchFilterRow
import io.github.droidkaigi.confsched.sessions.components.SearchNotFoundContent
import io.github.droidkaigi.confsched.sessions.components.SearchTopBar

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
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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
                        highlightWord = uiState.searchQuery,
                    )
                }
            }
        }
    }
}

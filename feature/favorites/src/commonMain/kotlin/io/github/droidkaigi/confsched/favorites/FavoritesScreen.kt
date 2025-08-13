package io.github.droidkaigi.confsched.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.excludeTop
import io.github.droidkaigi.confsched.droidkaigiui.extension.plus
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableList
import io.github.droidkaigi.confsched.favorites.components.FavoriteFilters
import io.github.droidkaigi.confsched.favorites.section.FavoriteEmpty
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.fake
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesScreenUiState,
    modifier: Modifier = Modifier,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onBookmarkClick: (TimetableItemId) -> Unit,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(FavoritesRes.string.favorite))
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            FavoriteFilters(
                allFilterSelected = uiState.filterState.allFilterSelected,
                day1FilterSelected = uiState.filterState.isDay1FilterSelected,
                day2FilterSelected = uiState.filterState.isDay2FilterSelected,
                onAllFilterChipClick = onAllFilterChipClick,
                onDay1FilterChipClick = onDay1FilterChipClick,
                onDay2FilterChipClick = onDay2FilterChipClick,
                modifier = Modifier.fillMaxWidth()
            )
            when (uiState.timetableContentState) {
                FavoritesScreenUiState.TimetableContentState.Empty -> {
                    FavoriteEmpty(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is FavoritesScreenUiState.TimetableContentState.FavoriteList -> {
                    TimetableList(
                        timetableItemMap = uiState.timetableContentState.timetableItemMap,
                        onTimetableItemClick = onTimetableItemClick,
                        onBookmarkClick = onBookmarkClick,
                        isBookmarked = { true },
                        isDateTagVisible = true,
                        contentPadding = WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues() + PaddingValues(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoritesScreenPreview() {
    KaigiPreviewContainer {
        FavoritesScreen(
            uiState = FavoritesScreenUiState(
                filterState = FavoritesScreenUiState.FilterState(
                    allFilterSelected = false,
                    currentDayFilter = persistentListOf(DroidKaigi2025Day.ConferenceDay1, DroidKaigi2025Day.ConferenceDay2)
                ),
                timetableContentState = FavoritesScreenUiState.TimetableContentState.FavoriteList(
                    timetableItemMap = persistentMapOf(
                        FavoritesScreenUiState.TimetableContentState.FavoriteList.TimeSlot(
                            day = DroidKaigi2025Day.ConferenceDay1,
                            startTimeString = "10:00",
                            endTimeString = "11:00",
                        ) to listOf(
                            TimetableItem.Session.fake(),
                        )
                    )
                )
            ),
            onTimetableItemClick = {},
            onBookmarkClick = {},
            onAllFilterChipClick = {},
            onDay1FilterChipClick = {},
            onDay2FilterChipClick = {},
        )
    }
}

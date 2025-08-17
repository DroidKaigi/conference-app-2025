package io.github.droidkaigi.confsched.sessions

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.excludeTop
import io.github.droidkaigi.confsched.droidkaigiui.extension.plus
import io.github.droidkaigi.confsched.droidkaigiui.layout.CollapsingHeaderLayout
import io.github.droidkaigi.confsched.droidkaigiui.layout.rememberCollapsingHeaderEnterAlwaysState
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableList
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableUiType
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableTopAppBar
import io.github.droidkaigi.confsched.sessions.grid.TimetableGrid
import io.github.droidkaigi.confsched.sessions.grid.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableScreen(
    uiState: TimetableScreenUiState,
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onBookmarkClick: (sessionId: String) -> Unit,
    onTimetableUiChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val collapsingState = rememberCollapsingHeaderEnterAlwaysState()
    val lazyListState = rememberLazyListState()

    val completelyScrolledToTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                lazyListState.firstVisibleItemScrollOffset == 0 &&
                collapsingState.collapsingOffsetY == 0f
        }
    }

    val headerBackgroundColor by animateColorAsState(
        targetValue = if (completelyScrolledToTop) Color.Transparent else MaterialTheme.colorScheme.surface,
    )

    Scaffold(
        topBar = {
            TimetableTopAppBar(
                timetableUiType = uiState.uiType,
                onSearchClick = onSearchClick,
                onUiTypeChangeClick = onTimetableUiChangeClick,
                modifier = Modifier.background(headerBackgroundColor),
            )
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(),
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        TimetableBackground()
        CollapsingHeaderLayout(
            state = collapsingState,
            headerContent = {
                var selectedDay by remember { mutableStateOf(DroidKaigi2025Day.ConferenceDay1) }
                val conferenceDays = listOf(DroidKaigi2025Day.ConferenceDay1, DroidKaigi2025Day.ConferenceDay2)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBackgroundColor)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SingleChoiceSegmentedButtonRow {
                        conferenceDays.forEachIndexed { index, droidKaigi2025Day ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = conferenceDays.size,
                                ),
                                onClick = { selectedDay = droidKaigi2025Day },
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                                ),
                                selected = selectedDay == droidKaigi2025Day,
                                modifier = Modifier
                                    .height(TimetableDefaults.dayTabHeight)
                                    .width(TimetableDefaults.dayTabWidth),
                            ) {
                                Text(droidKaigi2025Day.monthAndDay())
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(paddingValues),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(it),
            ) {
                when (uiState.timetable) {
                    is TimetableUiState.Empty -> Text("Empty")
                    is TimetableUiState.GridTimetable -> {
                        TimetableGrid(
                            timetable = requireNotNull(uiState.timetable.timetableGridUiState[uiState.timetable.selectedDay]).timetable,
                            timeLine = null, // TODO
                            selectedDay = uiState.timetable.selectedDay,
                            contentPadding = WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues(),
                        )
                    }

                    is TimetableUiState.ListTimetable -> {
                        val timetableListUiState = requireNotNull(uiState.timetable.timetableListUiStates[uiState.timetable.selectedDay])
                        TimetableList(
                            timetableItemMap = timetableListUiState.timetableItemMap,
                            onTimetableItemClick = onTimetableItemClick,
                            onBookmarkClick = { id -> onBookmarkClick(id.value) },
                            isBookmarked = { id -> timetableListUiState.timetable.bookmarks.contains(id) },
                            lazyListState = lazyListState,
                            contentPadding = WindowInsets.safeDrawingWithBottomNavBar.excludeTop().asPaddingValues() + PaddingValues(horizontal = 16.dp),
                            isDateTagVisible = false,
                        )
                    }
                }
            }
        }
    }
}

private object TimetableDefaults {
    val dayTabHeight = 40.dp
    val dayTabWidth = 104.dp
}

@Preview
@Composable
private fun TimetableScreenPreview() {
    KaigiPreviewContainer {
        TimetableScreen(
            uiState = TimetableScreenUiState(
                timetable = TimetableUiState.Empty,
                uiType = TimetableUiType.List,
            ),
            onBookmarkClick = {},
            onSearchClick = {},
            onTimetableItemClick = {},
            onTimetableUiChangeClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableScreenPreview_List() {
    KaigiPreviewContainer {
        TimetableScreen(
            uiState = TimetableScreenUiState(
                timetable = TimetableUiState.ListTimetable(
                    timetableListUiStates = mapOf(
                        DroidKaigi2025Day.ConferenceDay1 to TimetableListUiState(
                            persistentMapOf(),
                            Timetable.fake(),
                        ),
                        DroidKaigi2025Day.ConferenceDay2 to TimetableListUiState(
                            persistentMapOf(),
                            Timetable.fake(),
                        ),
                    ),
                    selectedDay = DroidKaigi2025Day.ConferenceDay1,
                ),
                uiType = TimetableUiType.List,
            ),
            onBookmarkClick = {},
            onSearchClick = {},
            onTimetableItemClick = {},
            onTimetableUiChangeClick = {},
        )
    }
}

@Preview
@Composable
private fun TimetableScreenPreview_Grid() {
    KaigiPreviewContainer {
        TimetableScreen(
            uiState = TimetableScreenUiState(
                timetable = TimetableUiState.GridTimetable(
                    timetableGridUiState = mapOf(
                        DroidKaigi2025Day.ConferenceDay1 to TimetableGridUiState(
                            Timetable.fake(),
                        ),
                        DroidKaigi2025Day.ConferenceDay2 to TimetableGridUiState(
                            Timetable.fake(),
                        ),
                    ),
                    selectedDay = DroidKaigi2025Day.ConferenceDay1,
                ),
                uiType = TimetableUiType.Grid,
            ),
            onBookmarkClick = {},
            onSearchClick = {},
            onTimetableItemClick = {},
            onTimetableUiChangeClick = {},
        )
    }
}

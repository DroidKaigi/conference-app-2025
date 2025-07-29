package io.github.droidkaigi.confsched.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableUiType
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableTopAppBar
import io.github.droidkaigi.confsched.sessions.section.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableList
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableScreen(
    uiState: TimetableScreenUiState,
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    onBookmarkClick: (sessionId: String, isBookmarked: Boolean) -> Unit,
    onTimetableUiChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TimetableTopAppBar(
                timetableUiType = uiState.uiType,
                onSearchClick = onSearchClick,
                onUiTypeChangeClick = onTimetableUiChangeClick,
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        Background()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            var selectedDay by remember { mutableStateOf(DroidKaigi2025Day.ConferenceDay1) }
            val conferenceDays = listOf(DroidKaigi2025Day.ConferenceDay1, DroidKaigi2025Day.ConferenceDay2)

            Text("Diff test")

            SingleChoiceSegmentedButtonRow {
                conferenceDays.forEachIndexed { index, droidKaigi2025Day ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = conferenceDays.size,
                        ),
                        onClick = { selectedDay = droidKaigi2025Day },
                        colors = SegmentedButtonDefaults.colors(
                            inactiveContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        selected = selectedDay == droidKaigi2025Day,
                        modifier = Modifier
                            .height(TimetableDefaults.dayTabHeight)
                            .width(TimetableDefaults.dayTabWidth)
                    ) {
                        Text(droidKaigi2025Day.monthAndDay())
                    }
                }
            }
            when (uiState.timetable) {
                is TimetableUiState.Empty -> Text("Empty")
                is TimetableUiState.GridTimetable -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        uiState.timetable.timetableGridUiState.forEach { (day, timetableDayData) ->
                            item {
                                Text(day.name)
                            }
                            items(timetableDayData.timetable.timetableItems) { item ->
                                TextButton(
                                    onClick = {
                                        onTimetableItemClick(item.id)
                                    }
                                ) { Text(item.title.jaTitle) }
                            }
                            item {
                                HorizontalDivider()
                            }
                        }
                    }
                }

                is TimetableUiState.ListTimetable -> {
                    TimetableList(
                        uiState = requireNotNull(uiState.timetable.timetableListUiStates[uiState.timetable.selectedDay]),
                        onTimetableItemClick = { onTimetableItemClick(it.id) },
                        onBookmarkClick = { timetableItem, isBookmarked ->
                            onBookmarkClick(timetableItem.id.value, isBookmarked)
                        },
                        contentPadding = WindowInsets.navigationBars.add(WindowInsets(left = 16.dp, right = 16.dp)).asPaddingValues()
                    )
                }
            }
        }
    }
}

@Composable
private fun Background() {
    Image(
        painter = painterResource(SessionsRes.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().alpha(0.18f),
    )
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
            onBookmarkClick = { _, _ -> },
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
            onBookmarkClick = { _, _ -> },
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
            onBookmarkClick = { _, _ -> },
            onSearchClick = {},
            onTimetableItemClick = {},
            onTimetableUiChangeClick = {},
        )
    }
}

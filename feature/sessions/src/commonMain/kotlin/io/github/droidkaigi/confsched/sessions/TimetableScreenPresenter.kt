package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Filters
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableUiType
import io.github.droidkaigi.confsched.sessions.grid.LocalClock
import io.github.droidkaigi.confsched.sessions.grid.TimeLine
import io.github.droidkaigi.confsched.sessions.grid.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.toPersistentMap
import soil.query.compose.rememberMutation

@Composable
context(screenContext: TimetableScreenContext)
fun timetableScreenPresenter(
    eventFlow: EventFlow<TimetableScreenEvent>,
    timetable: Timetable,
): TimetableScreenUiState = providePresenterDefaults {
    val favoriteTimetableItemIdMutation = rememberMutation(screenContext.favoriteTimetableItemIdMutationKey)

    var uiType by rememberRetained { mutableStateOf(TimetableUiType.List) }
    var selectedDay by rememberRetained { mutableStateOf(DroidKaigi2025Day.ConferenceDay1) }

    val clock = LocalClock.current
    var timeLine by remember { mutableStateOf(TimeLine.now(clock)) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is TimetableScreenEvent.Bookmark -> {
                favoriteTimetableItemIdMutation.mutate(TimetableItemId(event.sessionId))
            }

            is TimetableScreenEvent.SelectTab -> selectedDay = event.day

            is TimetableScreenEvent.UiTypeChange -> {
                uiType = if (uiType == TimetableUiType.Grid) {
                    TimetableUiType.List
                } else {
                    TimetableUiType.Grid
                }
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        timeLine = TimeLine.now(clock)
    }

    TimetableScreenUiState(
        timetable = timetableSheet(
            sessionTimetable = timetable,
            uiType = uiType,
            selectedDay = selectedDay,
            timeLine = timeLine,
        ),
        uiType = uiType,
    )
}

private fun timetableSheet(
    sessionTimetable: Timetable,
    uiType: TimetableUiType,
    selectedDay: DroidKaigi2025Day,
    timeLine: TimeLine?,
): TimetableUiState {
    if (sessionTimetable.timetableItems.isEmpty()) {
        return TimetableUiState.Empty
    }

    return if (uiType == TimetableUiType.List) {
        TimetableUiState.ListTimetable(
            DroidKaigi2025Day.visibleDays().associateWith { day ->
                val sortAndGroupedTimetableItems = sessionTimetable.filtered(
                    Filters(
                        days = listOf(day),
                    ),
                ).timetableItems.groupBy {
                    TimetableListUiState.TimeSlot(
                        startTime = it.startsLocalTime,
                        endTime = it.endsLocalTime,
                    )
                }.mapValues { entries ->
                    entries.value.sortedWith(
                        compareBy({ it.day?.name.orEmpty() }, { it.startsTimeString }),
                    )
                }.toPersistentMap()
                TimetableListUiState(
                    timetableItemMap = sortAndGroupedTimetableItems,
                    timetable = sessionTimetable.dayTimetable(day),
                )
            },
            selectedDay = selectedDay,
        )
    } else {
        TimetableUiState.GridTimetable(
            timetableGridUiState = DroidKaigi2025Day.visibleDays().associateWith { day ->
                TimetableGridUiState(
                    timetable = sessionTimetable.dayTimetable(day),
                )
            },
            selectedDay = selectedDay,
            timeLine = timeLine,
        )
    }
}

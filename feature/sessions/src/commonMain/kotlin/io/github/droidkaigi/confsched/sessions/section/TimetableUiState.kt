package io.github.droidkaigi.confsched.sessions.section

import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.sessions.grid.TimeLine
import io.github.droidkaigi.confsched.sessions.grid.TimetableGridUiState

sealed interface TimetableUiState {
    data object Empty : TimetableUiState

    data class ListTimetable(
        val timetableListUiStates: Map<DroidKaigi2025Day, TimetableListUiState>,
        val selectedDay: DroidKaigi2025Day,
    ) : TimetableUiState

    data class GridTimetable(
        val timetableGridUiState: Map<DroidKaigi2025Day, TimetableGridUiState>,
        val selectedDay: DroidKaigi2025Day,
        val timeLine: TimeLine?,
    ) : TimetableUiState
}

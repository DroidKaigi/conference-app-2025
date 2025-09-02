package io.github.droidkaigi.confsched.sessions

import io.github.droidkaigi.confsched.droidkaigiui.session.TimeSlotItem
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableCategory
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalTime

data class SearchScreenUiState(
    val searchQuery: String = "",
    val groupedSessions: PersistentMap<TimeSlot, List<TimetableItem>> = persistentMapOf(),
    val availableFilters: Filters = Filters.EMPTY,
    val hasSearchCriteria: Boolean = false,
    val bookmarks: PersistentSet<TimetableItemId> = persistentSetOf(),
) {
    data class TimeSlot(
        val startTime: LocalTime,
        val endTime: LocalTime,
    ) : TimeSlotItem {
        override val startTimeString: String = startTime.toTimetableTimeString()
        override val endTimeString: String = endTime.toTimetableTimeString()
        override val key: String = "$startTimeString-$endTimeString"

        private fun LocalTime.toTimetableTimeString(): String {
            return "$hour".padStart(2, '0') + ":" + "$minute".padStart(2, '0')
        }
    }

    data class Filters(
        val selectedDays: List<DroidKaigi2025Day> = emptyList(),
        val selectedCategories: List<TimetableCategory> = emptyList(),
        val selectedSessionTypes: List<TimetableSessionType> = emptyList(),
        val selectedLanguages: List<Lang> = emptyList(),
        val availableDays: List<DroidKaigi2025Day> = emptyList(),
        val availableCategories: List<TimetableCategory> = emptyList(),
        val availableSessionTypes: List<TimetableSessionType> = emptyList(),
        val availableLanguages: List<Lang> = emptyList(),
    ) {
        companion object {
            val EMPTY = Filters()
        }
    }
}

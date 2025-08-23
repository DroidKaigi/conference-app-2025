package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Filters
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableCategory
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentMap
import soil.query.compose.rememberMutation

@Composable
context(screenContext: SearchScreenContext)
fun searchScreenPresenter(
    eventFlow: EventFlow<SearchScreenEvent>,
    timetable: Timetable,
): SearchScreenUiState = providePresenterDefaults {
    var searchQuery by rememberRetained { mutableStateOf("") }
    var selectedDay by rememberRetained { mutableStateOf<DroidKaigi2025Day?>(null) }
    var selectedCategory by rememberRetained { mutableStateOf<TimetableCategory?>(null) }
    var selectedSessionType by rememberRetained { mutableStateOf<TimetableSessionType?>(null) }
    var selectedLanguage by rememberRetained { mutableStateOf<Lang?>(null) }
    val favoriteTimetableItemIdMutation = rememberMutation(screenContext.favoriteTimetableItemIdMutationKey)

    EventEffect(eventFlow) { event ->
        when (event) {
            is SearchScreenEvent.Search -> searchQuery = event.query
            is SearchScreenEvent.ToggleFilter -> {
                when (val filter = event.filter) {
                    is SearchScreenEvent.Filter.Day -> {
                        selectedDay = if (selectedDay == filter.day) null else filter.day
                    }
                    is SearchScreenEvent.Filter.Category -> {
                        selectedCategory = if (selectedCategory == filter.category) null else filter.category
                    }
                    is SearchScreenEvent.Filter.SessionType -> {
                        selectedSessionType = if (selectedSessionType == filter.sessionType) null else filter.sessionType
                    }
                    is SearchScreenEvent.Filter.Language -> {
                        selectedLanguage = if (selectedLanguage == filter.language) null else filter.language
                    }
                }
            }
            is SearchScreenEvent.Bookmark -> {
                val targetId = TimetableItemId(event.sessionId)
                favoriteTimetableItemIdMutation.mutate(targetId)
            }
            SearchScreenEvent.ClearFilters -> {
                selectedDay = null
                selectedCategory = null
                selectedSessionType = null
                selectedLanguage = null
            }
        }
    }

    val hasSearchCriteria = searchQuery.isNotEmpty() ||
        selectedDay != null ||
        selectedCategory != null ||
        selectedSessionType != null ||
        selectedLanguage != null

    val filteredTimetable = if (hasSearchCriteria) {
        timetable.filtered(
            Filters(
                searchWord = searchQuery,
                days = selectedDay?.let { listOf(it) } ?: emptyList(),
                categories = selectedCategory?.let { listOf(it) } ?: emptyList(),
                sessionTypes = selectedSessionType?.let { listOf(it) } ?: emptyList(),
                languages = selectedLanguage?.let { listOf(it) } ?: emptyList(),
            ),
        )
    } else {
        timetable.copy(timetableItems = persistentListOf())
    }

    val groupedSessions = filteredTimetable.timetableItems
        .groupBy { session ->
            SearchScreenUiState.TimeSlot(
                startTime = session.startsLocalTime,
                endTime = session.endsLocalTime,
            )
        }
        .mapValues { entries ->
            entries.value.sortedWith(
                compareBy({ it.day?.name.orEmpty() }, { it.startsTimeString }),
            )
        }
        .toPersistentMap()

    SearchScreenUiState(
        searchQuery = searchQuery,
        groupedSessions = groupedSessions,
        availableFilters = SearchScreenUiState.Filters(
            selectedDay = selectedDay,
            selectedCategory = selectedCategory,
            selectedSessionType = selectedSessionType,
            selectedLanguage = selectedLanguage,
            availableDays = DroidKaigi2025Day.visibleDays(),
            availableCategories = timetable.timetableItems.mapNotNull { it.category }.distinct(),
            availableSessionTypes = timetable.timetableItems.map { it.sessionType }.distinct(),
            availableLanguages = listOf(Lang.JAPANESE, Lang.ENGLISH),
        ),
        hasSearchCriteria = hasSearchCriteria,
        bookmarks = timetable.bookmarks,
    )
}

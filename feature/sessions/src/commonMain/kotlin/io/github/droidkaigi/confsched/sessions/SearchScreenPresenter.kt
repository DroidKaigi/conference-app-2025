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
    var searchQuery by rememberRetained(
        key = "searchQuery:${screenContext.openedAtEpochMillis}",
    ) { mutableStateOf("") }
    var selectedDays by rememberRetained(
        key = "selectedDays:${screenContext.openedAtEpochMillis}",
    ) { mutableStateOf<List<DroidKaigi2025Day>>(emptyList()) }
    var selectedCategories by rememberRetained(
        key = "selectedCategories:${screenContext.openedAtEpochMillis}",
    ) { mutableStateOf<List<TimetableCategory>>(emptyList()) }
    var selectedSessionTypes by rememberRetained(
        key = "selectedSessionTypes:${screenContext.openedAtEpochMillis}",
    ) { mutableStateOf<List<TimetableSessionType>>(emptyList()) }
    var selectedLanguages by rememberRetained(
        key = "selectedLanguages:${screenContext.openedAtEpochMillis}",
    ) { mutableStateOf<List<Lang>>(emptyList()) }
    val favoriteTimetableItemIdMutation = rememberMutation(screenContext.favoriteTimetableItemIdMutationKey)

    EventEffect(eventFlow) { event ->
        when (event) {
            is SearchScreenEvent.Search -> searchQuery = event.query
            is SearchScreenEvent.ToggleFilter -> {
                when (val filter = event.filter) {
                    is SearchScreenEvent.Filter.Day -> {
                        selectedDays = selectedDays.toggle(filter.day)
                    }

                    is SearchScreenEvent.Filter.Category -> {
                        selectedCategories = selectedCategories.toggle(filter.category)
                    }

                    is SearchScreenEvent.Filter.SessionType -> {
                        selectedSessionTypes = selectedSessionTypes.toggle(filter.sessionType)
                    }

                    is SearchScreenEvent.Filter.Language -> {
                        selectedLanguages = selectedLanguages.toggle(filter.language)
                    }
                }
            }

            is SearchScreenEvent.Bookmark -> {
                val targetId = TimetableItemId(event.sessionId)
                favoriteTimetableItemIdMutation.mutate(targetId)
            }

            SearchScreenEvent.ClearFilters -> {
                selectedDays = emptyList()
                selectedCategories = emptyList()
                selectedSessionTypes = emptyList()
                selectedLanguages = emptyList()
            }
        }
    }

    val hasSearchCriteria = searchQuery.isNotEmpty() ||
        selectedDays.isNotEmpty() ||
        selectedCategories.isNotEmpty() ||
        selectedSessionTypes.isNotEmpty() ||
        selectedLanguages.isNotEmpty()

    val filteredTimetable = if (hasSearchCriteria) {
        timetable.filtered(
            Filters(
                searchWord = searchQuery,
                days = selectedDays,
                categories = selectedCategories,
                sessionTypes = selectedSessionTypes,
                languages = selectedLanguages,
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
            selectedDays = selectedDays,
            selectedCategories = selectedCategories,
            selectedSessionTypes = selectedSessionTypes,
            selectedLanguages = selectedLanguages,
            availableDays = DroidKaigi2025Day.visibleDays(),
            availableCategories = timetable.timetableItems.map { it.category }.distinct(),
            availableSessionTypes = timetable.timetableItems.map { it.sessionType }.distinct(),
            availableLanguages = listOf(Lang.JAPANESE, Lang.ENGLISH),
        ),
        hasSearchCriteria = hasSearchCriteria,
        bookmarks = timetable.bookmarks,
    )
}

private fun <T> List<T>.toggle(item: T): List<T> {
    return if (contains(item)) {
        minus(item)
    } else {
        plus(item)
    }
}

package io.github.droidkaigi.confsched.naventry

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.navigation.listDetailSceneStrategyDetailPaneMetaData
import io.github.droidkaigi.confsched.navigation.listDetailSceneStrategyListPaneMetaData
import io.github.droidkaigi.confsched.navkey.SearchNavKey
import io.github.droidkaigi.confsched.navkey.TimetableItemDetailNavKey
import io.github.droidkaigi.confsched.navkey.TimetableNavKey
import io.github.droidkaigi.confsched.sessions.SearchScreenRoot
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenRoot
import io.github.droidkaigi.confsched.sessions.TimetableScreenRoot
import io.github.droidkaigi.confsched.sessions.rememberSearchScreenContextRetained
import io.github.droidkaigi.confsched.sessions.rememberTimetableItemDetailScreenContextRetained
import io.github.droidkaigi.confsched.sessions.rememberTimetableScreenContextRetained

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.sessionEntries(
    onBackClick: () -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onLinkClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    timeTableEntryMetadata: Map<String, Any> = emptyMap(),
) {
    timetableEntry(
        onSearchClick = onSearchClick,
        onTimetableItemClick = onTimetableItemClick,
        metadata = timeTableEntryMetadata,
    )
    timetableItemDetailEntry(
        onBackClick = onBackClick,
        onAddCalendarClick = onAddCalendarClick,
        onShareClick = onShareClick,
        onLinkClick = onLinkClick,
    )
    searchEntry(
        onBackClick = onBackClick,
        onTimetableItemClick = onTimetableItemClick,
    )
}

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.timetableEntry(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<TimetableNavKey>(metadata = listDetailSceneStrategyListPaneMetaData() + metadata) {
        with(rememberTimetableScreenContextRetained()) {
            TimetableScreenRoot(
                onSearchClick = onSearchClick,
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.timetableItemDetailEntry(
    onBackClick: () -> Unit,
    onAddCalendarClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
    onLinkClick: (String) -> Unit,
) {
    entry<TimetableItemDetailNavKey>(metadata = listDetailSceneStrategyDetailPaneMetaData()) {
        with(rememberTimetableItemDetailScreenContextRetained(it.id)) {
            TimetableItemDetailScreenRoot(
                onBackClick = onBackClick,
                onAddCalendarClick = onAddCalendarClick,
                onShareClick = onShareClick,
                onLinkClick = onLinkClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.searchEntry(
    onBackClick: () -> Unit = {},
    onTimetableItemClick: (TimetableItemId) -> Unit = {},
) {
    entry<SearchNavKey>(metadata = listDetailSceneStrategyListPaneMetaData()) {
        with(rememberSearchScreenContextRetained()) {
            SearchScreenRoot(
                onBackClick = onBackClick,
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}

package io.github.droidkaigi.confsched.naventry

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.eventmap.EventMapScreenRoot
import io.github.droidkaigi.confsched.eventmap.rememberEventMapScreenContextRetained
import io.github.droidkaigi.confsched.navkey.EventMapNavKey

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.eventMapEntry(
    onClickReadMore: (url: String) -> Unit,
) {
    entry<EventMapNavKey> {
        with(appGraph.rememberEventMapScreenContextRetained()) {
            EventMapScreenRoot(
                onClickReadMore = onClickReadMore,
            )
        }
    }
}

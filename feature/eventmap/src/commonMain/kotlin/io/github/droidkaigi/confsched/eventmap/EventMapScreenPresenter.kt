package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import kotlinx.collections.immutable.PersistentList

context(_: EventMapScreenContext)
@Composable
fun eventMapScreenPresenter(
    events: PersistentList<EventMapEvent>,
    eventFlow: EventFlow<EventMapScreenEvent>,
) = providePresenterDefaults {
    var selectedFloor by rememberSaveable { mutableStateOf(FloorLevel.Ground) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is EventMapScreenEvent.SelectFloor -> selectedFloor = event.floor
        }
    }

    EventMapUiState(
        events = events,
        selectedFloor = selectedFloor,
    )
}

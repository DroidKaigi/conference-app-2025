package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import kotlinx.collections.immutable.PersistentList

@Composable
context(_: EventMapScreenContext)
fun eventMapScreenPresenter(
    events: PersistentList<EventMapEvent>,
    eventFlow: EventFlow<EventMapScreenEvent>,
) = providePresenterDefaults {
    val floorSaver: Saver<MutableState<FloorLevel>, String> = Saver(
        save = { it.value.name },
        restore = { mutableStateOf(FloorLevel.valueOf(it)) },
    )
    var selectedFloor by rememberSaveable(saver = floorSaver) { mutableStateOf(FloorLevel.Ground) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is EventMapScreenEvent.SelectFloor -> selectedFloor = event.floor
        }
    }

    EventMapScreenUiState(
        events = events,
        selectedFloor = selectedFloor,
    )
}

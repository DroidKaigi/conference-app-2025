package io.github.droidkaigi.confsched.eventmap

import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import io.github.droidkaigi.confsched.model.eventmap.FloorLevel
import kotlinx.collections.immutable.PersistentList

data class EventMapScreenUiState(
    val events: PersistentList<EventMapEvent>,
    val selectedFloor: FloorLevel,
)

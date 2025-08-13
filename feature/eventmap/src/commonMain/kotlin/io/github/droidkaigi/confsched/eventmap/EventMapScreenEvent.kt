package io.github.droidkaigi.confsched.eventmap

import io.github.droidkaigi.confsched.model.eventmap.FloorLevel

sealed interface EventMapScreenEvent {
    data class SelectFloor(val floor: FloorLevel) : EventMapScreenEvent
}

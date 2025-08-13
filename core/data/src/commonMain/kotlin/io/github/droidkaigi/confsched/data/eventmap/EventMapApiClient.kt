package io.github.droidkaigi.confsched.data.eventmap

import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import kotlinx.collections.immutable.PersistentList

public interface EventMapApiClient {

    public suspend fun eventMapEvents(): PersistentList<EventMapEvent>
}

package io.github.droidkaigi.confsched.data.eventmap

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.eventmap.EventMapQueryKey
import soil.query.QueryId
import soil.query.buildQueryKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultEventMapQueryKey(
    private val eventMapApiClient: EventMapApiClient,
) : EventMapQueryKey by buildQueryKey(
    id = QueryId("event_map"),
    fetch = { eventMapApiClient.eventMapEvents() }
)

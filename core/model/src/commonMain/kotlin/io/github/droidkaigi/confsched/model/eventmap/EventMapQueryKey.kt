package io.github.droidkaigi.confsched.model.eventmap

import kotlinx.collections.immutable.PersistentList
import soil.query.QueryKey

typealias EventMapQueryKey = QueryKey<PersistentList<EventMapEvent>>

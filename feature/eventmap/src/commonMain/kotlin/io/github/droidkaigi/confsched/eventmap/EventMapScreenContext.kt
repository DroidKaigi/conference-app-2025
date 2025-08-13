package io.github.droidkaigi.confsched.eventmap

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesGraphExtension
import io.github.droidkaigi.confsched.common.scope.EventMapScope
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.model.eventmap.EventMapQueryKey

@ContributesGraphExtension(EventMapScope::class)
interface EventMapScreenContext : ScreenContext {
    val eventMapQueryKey: EventMapQueryKey

    @ContributesGraphExtension.Factory(AppScope::class)
    fun interface Factory {
        fun create(): EventMapScreenContext
    }
}

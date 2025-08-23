package io.github.droidkaigi.confsched.sessions

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesGraphExtension
import io.github.droidkaigi.confsched.common.scope.SearchScope
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey

@ContributesGraphExtension(SearchScope::class)
interface SearchScreenContext : ScreenContext {
    val timetableQueryKey: TimetableQueryKey
    val favoriteTimetableIdsSubscriptionKey: FavoriteTimetableIdsSubscriptionKey
    val favoriteTimetableItemIdMutationKey: FavoriteTimetableItemIdMutationKey

    @ContributesGraphExtension.Factory(AppScope::class)
    fun interface Factory {
        fun createSearchScreenContext(): SearchScreenContext
    }
}

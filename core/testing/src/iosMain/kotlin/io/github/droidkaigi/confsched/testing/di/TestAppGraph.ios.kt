package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.core.DataStorePathProducer
import io.github.droidkaigi.confsched.data.core.defaultJson
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultTimetableQueryKey
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
    excludes = [
        DefaultSessionsApiClient::class,
        DefaultContributorsApiClient::class,
        CoroutineDispatcher::class,
    ],
)
internal interface IosTestAppGraph : TestAppGraph {
    @Binds
    val DefaultTimetableQueryKey.bind: TimetableQueryKey

    @Binds
    val DefaultFavoriteTimetableIdsSubscriptionKey.bind: FavoriteTimetableIdsSubscriptionKey

    @Binds
    val DefaultFavoriteTimetableItemIdMutationKey.bind: FavoriteTimetableItemIdMutationKey

    @Provides
    fun provideJson(): Json {
        return defaultJson()
    }

    @OptIn(ExperimentalForeignApi::class)
    @Provides
    fun providesDataStorePathProducer(): DataStorePathProducer {
        return DataStorePathProducer { fileName ->
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/$fileName"
        }
    }
}

internal actual fun createTestAppGraph(): TestAppGraph {
    return createGraph<IosTestAppGraph>()
}

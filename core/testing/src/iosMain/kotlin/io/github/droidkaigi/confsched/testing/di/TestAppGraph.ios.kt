package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.common.scope.TimetableDetailScope
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.about.FakeBuildConfigProvider
import io.github.droidkaigi.confsched.data.about.FakeLicensesJsonReader
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsQueryKey
import io.github.droidkaigi.confsched.data.core.DataStorePathProducer
import io.github.droidkaigi.confsched.data.core.defaultJson
import io.github.droidkaigi.confsched.data.eventmap.DefaultEventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.DefaultEventMapQueryKey
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultTimetableItemQueryKey
import io.github.droidkaigi.confsched.data.sessions.DefaultTimetableQueryKey
import io.github.droidkaigi.confsched.data.settings.DefaultSettingsMutationKey
import io.github.droidkaigi.confsched.data.settings.DefaultSettingsSubscriptionKey
import io.github.droidkaigi.confsched.data.staff.DefaultStaffApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffQueryKey
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.contributors.ContributorsQueryKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.data.TimetableItemQueryKey
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey
import io.github.droidkaigi.confsched.model.eventmap.EventMapQueryKey
import io.github.droidkaigi.confsched.model.settings.SettingsMutationKey
import io.github.droidkaigi.confsched.model.settings.SettingsSubscriptionKey
import io.github.droidkaigi.confsched.model.staff.StaffQueryKey
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
        DefaultEventMapApiClient::class,
        DefaultStaffApiClient::class,
        CoroutineDispatcher::class,
    ],
)
internal interface IosTestAppGraph : TestAppGraph {
    @Binds
    val DefaultTimetableQueryKey.bind: TimetableQueryKey

    @Binds
    val DefaultStaffQueryKey.bind: StaffQueryKey

    @Binds
    val DefaultFavoriteTimetableIdsSubscriptionKey.bind: FavoriteTimetableIdsSubscriptionKey

    @Binds
    val DefaultFavoriteTimetableItemIdMutationKey.bind: FavoriteTimetableItemIdMutationKey

    @Binds
    val DefaultSettingsSubscriptionKey.bind: SettingsSubscriptionKey

    @Binds
    val DefaultSettingsMutationKey.bind: SettingsMutationKey

    @Binds
    val DefaultContributorsQueryKey.bind: ContributorsQueryKey

    @Binds
    val DefaultEventMapQueryKey.bind: EventMapQueryKey

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

    @Provides
    fun provideFakeBuildConfigProvider(): FakeBuildConfigProvider = FakeBuildConfigProvider()

    @Provides
    fun provideFakeLicensesJsonReader(): FakeLicensesJsonReader = FakeLicensesJsonReader()
}

@ContributesTo(TimetableDetailScope::class)
interface IosTimetableItemDetailGraph {
    @Binds
    val DefaultTimetableItemQueryKey.bind: TimetableItemQueryKey
}

internal actual fun createTestAppGraph(): TestAppGraph {
    return createGraph<IosTestAppGraph>()
}

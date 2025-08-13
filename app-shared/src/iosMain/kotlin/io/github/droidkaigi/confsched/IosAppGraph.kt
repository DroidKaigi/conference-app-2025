package io.github.droidkaigi.confsched

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import de.jensklingenberg.ktorfit.Ktorfit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.common.scope.TimetableDetailScope
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.DataStoreDependencyProviders
import io.github.droidkaigi.confsched.data.SessionCacheDataStoreQualifier
import io.github.droidkaigi.confsched.data.UserDataStoreQualifier
import io.github.droidkaigi.confsched.data.about.DefaultLicensesQueryKey
import io.github.droidkaigi.confsched.data.annotations.IoDispatcher
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsQueryKey
import io.github.droidkaigi.confsched.data.core.DataStorePathProducer
import io.github.droidkaigi.confsched.data.core.defaultJson
import io.github.droidkaigi.confsched.data.core.defaultKtorConfig
import io.github.droidkaigi.confsched.data.eventmap.DefaultEventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.DefaultEventMapQueryKey
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiClient
import io.github.droidkaigi.confsched.data.profile.DefaultProfileMutationKey
import io.github.droidkaigi.confsched.data.profile.DefaultProfileSubscriptionKey
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultTimetableItemQueryKey
import io.github.droidkaigi.confsched.data.sessions.DefaultTimetableQueryKey
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.sponsors.DefaultSponsorsApiClient
import io.github.droidkaigi.confsched.data.sponsors.DefaultSponsorsQueryKey
import io.github.droidkaigi.confsched.data.sponsors.SponsorsApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffQueryKey
import io.github.droidkaigi.confsched.data.staff.StaffApiClient
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.data.user.DefaultFavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.about.LicensesQueryKey
import io.github.droidkaigi.confsched.model.contributors.ContributorsQueryKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableItemIdMutationKey
import io.github.droidkaigi.confsched.model.data.TimetableItemQueryKey
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey
import io.github.droidkaigi.confsched.model.eventmap.EventMapQueryKey
import io.github.droidkaigi.confsched.model.profile.ProfileMutationKey
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import io.github.droidkaigi.confsched.model.sponsors.SponsorsQueryKey
import io.github.droidkaigi.confsched.model.staff.StaffQueryKey
import io.github.droidkaigi.confsched.repository.ContributorsRepository
import io.github.droidkaigi.confsched.repository.EventMapRepository
import io.github.droidkaigi.confsched.repository.SessionsRepository
import io.github.droidkaigi.confsched.repository.SponsorsRepository
import io.github.droidkaigi.confsched.repository.StaffRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import soil.query.SwrCachePlus
import soil.query.SwrCacheScope
import soil.query.SwrClientPlus
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.annotation.InternalSoilQueryApi

/**
 * The iOS dependency graph cannot currently be resolved by the compiler plugin.
 * Therefore, we need to define the iOS dependency graph manually.
 * For more details, see: https://github.com/ZacSweers/metro/issues/460
 */
@OptIn(BetaInteropApi::class)
@ExportObjCClass
@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
)
interface IosAppGraph : AppGraph {
    val sessionsRepository: SessionsRepository
    val contributorsRepository: ContributorsRepository
    val sponsorsRepository: SponsorsRepository
    val staffRepository: StaffRepository
    val eventMapRepository: EventMapRepository

    @Named("apiBaseUrl")
    @Provides
    fun provideApiBaseUrl(): String {
        return "https://ssot-api-staging.an.r.appspot.com/"
    }

    @Binds
    val DefaultTimetableQueryKey.bind: TimetableQueryKey

    @Binds
    val DefaultFavoriteTimetableIdsSubscriptionKey.bind: FavoriteTimetableIdsSubscriptionKey

    @Binds
    val DefaultSessionsApiClient.bind: SessionsApiClient

    @Binds
    val DefaultFavoriteTimetableItemIdMutationKey.bind: FavoriteTimetableItemIdMutationKey

    @Binds
    val DefaultContributorsApiClient.bind: ContributorsApiClient

    @Binds
    val DefaultContributorsQueryKey.bind: ContributorsQueryKey

    @Binds
    val DefaultSponsorsApiClient.bind: SponsorsApiClient

    @Binds
    val DefaultSponsorsQueryKey.bind: SponsorsQueryKey

    @Binds
    val DefaultStaffApiClient.bind: StaffApiClient

    @Binds
    val DefaultStaffQueryKey.bind: StaffQueryKey

    @Binds
    val DefaultLicensesQueryKey.bind: LicensesQueryKey

    @Binds
    val DefaultEventMapApiClient.bind: EventMapApiClient

    @Binds
    val DefaultEventMapQueryKey.bind: EventMapQueryKey

    @Binds
    val DefaultProfileSubscriptionKey.bind: ProfileSubscriptionKey

    @Binds
    val DefaultProfileMutationKey.bind: ProfileMutationKey

    @Provides
    fun provideJson(): Json {
        return defaultJson()
    }

    @Provides
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(Darwin) {
            defaultKtorConfig(json)
        }
    }

    @Provides
    fun provideKtorfit(
        httpClient: HttpClient,
        @Named("apiBaseUrl") apiBaseUrl: String,
    ): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl(apiBaseUrl)
            .build()
    }

    @SingleIn(DataScope::class)
    @UserDataStoreQualifier
    @Provides
    fun provideDataStorePreferences(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = ReplaceFileCorruptionHandler({ emptyPreferences() }),
            migrations = emptyList(),
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { dataStorePathProducer.producePath(DataStoreDependencyProviders.DATA_STORE_PREFERENCE_FILE_NAME).toPath() },
        )
    }

    @SingleIn(DataScope::class)
    @SessionCacheDataStoreQualifier
    @Provides
    fun provideSessionCacheDataStore(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = ReplaceFileCorruptionHandler({ emptyPreferences() }),
            migrations = emptyList(),
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { dataStorePathProducer.producePath(DataStoreDependencyProviders.DATA_STORE_CACHE_PREFERENCE_FILE_NAME).toPath() },
        )
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

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        // Since Kotlin/Native doesn't support Dispatchers.IO, we use Dispatchers.Default instead.
        return Dispatchers.Default
    }

    @OptIn(ExperimentalSoilQueryApi::class, InternalSoilQueryApi::class)
    @SingleIn(AppScope::class)
    @Provides
    fun provideSwrClientPlus(swrCacheScope: SwrCacheScope): SwrClientPlus {
        return SwrCachePlus(swrCacheScope)
    }

    @Provides
    fun provideSwrCacheScope(): SwrCacheScope {
        return SwrCacheScope()
    }
}

@ContributesTo(TimetableDetailScope::class)
interface IosTimetableItemDetailGraph {
    @Binds
    val DefaultTimetableItemQueryKey.bind: TimetableItemQueryKey
}

fun createIosAppGraph(): IosAppGraph {
    return createGraph()
}

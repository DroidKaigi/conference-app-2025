package io.github.droidkaigi.confsched.testing.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.DataStoreDependencyProviders
import io.github.droidkaigi.confsched.data.ProfileDataStoreQualifier
import io.github.droidkaigi.confsched.data.SessionCacheDataStoreQualifier
import io.github.droidkaigi.confsched.data.SettingsDataStoreQualifier
import io.github.droidkaigi.confsched.data.UserDataStoreQualifier
import io.github.droidkaigi.confsched.data.annotations.IoDispatcher
import io.github.droidkaigi.confsched.data.core.DataStorePathProducer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

@ContributesTo(DataScope::class, replaces = [DataStoreDependencyProviders::class])
interface TestDataStoreDependencyProviders {
    @UserDataStoreQualifier
    @Provides
    fun provideDataStorePreferences(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return InMemoryDataStore(emptyPreferences())
    }

    @SessionCacheDataStoreQualifier
    @Provides
    fun provideSessionCacheDataStore(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return InMemoryDataStore(emptyPreferences())
    }

    @SettingsDataStoreQualifier
    @Provides
    fun provideSettingsDataStore(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return InMemoryDataStore(emptyPreferences())
    }

    @ProfileDataStoreQualifier
    @Provides
    fun provideProfileDataStore(
        dataStorePathProducer: DataStorePathProducer,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataStore<Preferences> {
        return InMemoryDataStore(emptyPreferences())
    }
}

private class InMemoryDataStore<T>(initialValue: T) : DataStore<T> {
    override val data = MutableStateFlow(initialValue)
    override suspend fun updateData(
        transform: suspend (t: T) -> T,
    ): T = data.updateAndGet { transform(it) }
}

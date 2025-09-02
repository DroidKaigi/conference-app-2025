package io.github.droidkaigi.confsched.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.SettingsDataStoreQualifier
import io.github.droidkaigi.confsched.model.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json

@SingleIn(DataScope::class)
@Inject
public class SettingsDataStore(
    @param:SettingsDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    public suspend fun save(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[DATA_STORE_SETTINGS_KEY] = json.encodeToString(settings)
        }
    }

    public fun getSettingsStream(): Flow<Settings> {
        return dataStore.data.mapNotNull { preferences ->
            val cache = preferences[DATA_STORE_SETTINGS_KEY] ?: json.encodeToString(Settings.Default)
            json.decodeFromString<Settings>(cache)
        }
    }

    private companion object {
        private val DATA_STORE_SETTINGS_KEY = stringPreferencesKey("DATA_STORE_SETTINGS_KEY")
    }
}
